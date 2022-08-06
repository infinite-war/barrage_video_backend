package top.houry.netty.barrage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.houry.netty.barrage.annotation.BarrageAnnotation;
import top.houry.netty.barrage.bo.BarrageMsgBo;
import top.houry.netty.barrage.consts.BarrageMsgTypeConst;
import top.houry.netty.barrage.consts.BarrageRedisKeyConst;
import top.houry.netty.barrage.consts.BarrageVideoConst;
import top.houry.netty.barrage.entity.BarrageMsg;
import top.houry.netty.barrage.proto.BarrageProto;
import top.houry.netty.barrage.service.IBarrageMsgSendToClientService;
import top.houry.netty.barrage.service.IBarrageMsgService;
import top.houry.netty.barrage.service.IBarrageMsgTypeService;
import top.houry.netty.barrage.utils.BarrageConnectInfoUtils;
import top.houry.netty.barrage.utils.BarrageMsgSensitiveUtils;
import top.houry.netty.barrage.utils.BarrageRedisUtils;

import java.util.Date;
import java.util.List;

/**
 * @Desc 服务器接收客户端发送过来的弹幕信息
 * @Author houruiyang
 * @Date 2022/1/18
 **/
@Service
@BarrageAnnotation(msgType = BarrageMsgTypeConst.WEB_CLIENT_BARRAGE_REQ)
@Slf4j
public class BarrageReceiveMsgServiceImpl implements IBarrageMsgTypeService {

    private IBarrageMsgSendToClientService barrageSendMsgToClientService;

    private IBarrageMsgService barrageMsgService;

    @Autowired
    public void setBarrageMsgService(IBarrageMsgService barrageMsgService) {
        this.barrageMsgService = barrageMsgService;
    }

    @Autowired
    public void setBarrageSendMsgToClientService(IBarrageMsgSendToClientService barrageSendMsgToClientService) {
        this.barrageSendMsgToClientService = barrageSendMsgToClientService;
    }

    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {
        try {
            BarrageProto.WebClientSendBarrageReq clientSendBarrage = BarrageProto.WebClientSendBarrageReq.parseFrom(barrage.getBytesData());
            log.info("[Req]-[BarrageReceiveMsgServiceImpl]-[dealWithBarrageMessage]-[params{}]", TextFormat.printToUnicodeString(clientSendBarrage));
            String msg = StrUtil.blankToDefault(clientSendBarrage.getMsg(), "我们都是追梦人");
            String msgColor = StrUtil.blankToDefault(clientSendBarrage.getMsgColor(), "#FFFFFF");
            String userId = StrUtil.blankToDefault(clientSendBarrage.getUserId(), "");
            String videId = StrUtil.blankToDefault(clientSendBarrage.getVideoId(), "");
            int msgPosition = ObjectUtil.defaultIfNull(clientSendBarrage.getMsgPosition(), 0);
            int msgVideoTime = ObjectUtil.defaultIfNull(clientSendBarrage.getMsgVideoTime(), 1);

            // 进行敏感词汇过滤替换
            msg = filterSensitiveMsg(msg);

            BarrageMsg barrageMsg = new BarrageMsg();
            barrageMsg.setMsgColor(msgColor);
            barrageMsg.setMsgContent(msg);
            barrageMsg.setMsgPosition(msgPosition);
            barrageMsg.setUserId(Long.parseLong(userId));
            barrageMsg.setVideoId(Long.parseLong(videId));
            barrageMsg.setVideoTime(0 < msgVideoTime ? msgVideoTime : 1);
            barrageMsg.setCreateTime(new Date());
            barrageMsg.setUpdateTime(new Date());
            barrageMsgService.saveBarrageMsg(barrageMsg);

            BarrageMsgBo barrageMsgBo = new BarrageMsgBo(msg, msgColor, msgPosition, userId, videId);
            BarrageRedisUtils.listPush(BarrageRedisKeyConst.BARRAGE_TOTAL_MSG_KEY + BarrageVideoConst.VIDE_ID, JSONUtil.toJsonStr(barrageMsgBo));

            notifyOtherUser(videId, ctx, barrageMsgBo);

        } catch (Exception e) {
            log.error("[Exception]-[BarrageReceiveMsgServiceImpl]-[dealWithBarrageMessage]", e);
        }
    }


    private void notifyOtherUser(String videId, ChannelHandlerContext ctx, BarrageMsgBo barrageMsgBo) {
        List<ChannelHandlerContext> channelHandlerContextLists = BarrageConnectInfoUtils.getChannelHandlerContextListByVideId(videId);
        if (CollectionUtils.isEmpty(channelHandlerContextLists)) {
            return;
        }
        channelHandlerContextLists.stream()
                .filter(v -> !v.equals(ctx) && v.channel().isActive())
                .forEach(v -> barrageSendMsgToClientService.sendMsg(barrageMsgBo, v));
    }

    private String filterSensitiveMsg(String msg) {
        return BarrageMsgSensitiveUtils.replaceSensitiveMsg(msg);
    }

}
