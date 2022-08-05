package top.houry.netty.barrage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.houry.netty.barrage.annotation.BarrageAnnotation;
import top.houry.netty.barrage.consts.BarrageMsgTypeConst;
import top.houry.netty.barrage.entity.BarrageMsg;
import top.houry.netty.barrage.proto.BarrageProto;
import top.houry.netty.barrage.service.IBarrageMsgService;
import top.houry.netty.barrage.service.IBarrageMsgTypeService;
import top.houry.netty.barrage.utils.BarrageDateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Author houruiyang
 * @Date 2022/8/5
 **/
@Service
@BarrageAnnotation(msgType = BarrageMsgTypeConst.WEB_CLIENT_BARRAGE_MSG_ROLLING_REQ)
@Slf4j
public class BarrageMsgRollingServiceImpl implements IBarrageMsgTypeService {

    private IBarrageMsgService barrageMsgService;

    @Autowired
    public void setBarrageMsgService(IBarrageMsgService barrageMsgService) {
        this.barrageMsgService = barrageMsgService;
    }

    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {
        try {
            BarrageProto.WebClientBarrageMsgRollingReq msgRollingReq = BarrageProto.WebClientBarrageMsgRollingReq.parseFrom(barrage.getBytesData());
            log.info("[Req]-[BarrageMsgRollingServiceImpl]-[dealWithBarrageMessage]-[params:{}]",  TextFormat.printToUnicodeString(msgRollingReq));

            String videoId = msgRollingReq.getVideoId();
            String currentVideoTime = msgRollingReq.getCurrentVideoTime();
            List<BarrageMsg> barrageMsgList = barrageMsgService.getRollingBarrages(videoId, currentVideoTime);

            List<BarrageProto.BarrageHistoryMessage> msgList = new ArrayList<>();
            barrageMsgList.forEach(v ->{
                BarrageProto.BarrageHistoryMessage.Builder message = BarrageProto.BarrageHistoryMessage.newBuilder();
                message.setMsg(v.getMsgColor());
                message.setCreateTime(BarrageDateUtils.dateToString(v.getCreateTime(), BarrageDateUtils.DateType.PURE_DATE_MD_HM_PATTERN));
                message.setMsgColor(v.getMsgColor());
                message.setSendTime(BarrageDateUtils.secondToNormTime(ObjectUtil.defaultIfNull(v.getVideoTime(), 0L)));
                message.setMsg(v.getMsgContent());
                msgList.add(message.build());
            });

            BarrageProto.Barrage.Builder builder = BarrageProto.Barrage.newBuilder();
            BarrageProto.WebClientBarrageMsgRollingResp.Builder resp = BarrageProto.WebClientBarrageMsgRollingResp.newBuilder();
            resp.addAllList(msgList);
            builder.setMsgType(BarrageMsgTypeConst.WEB_CLIENT_BARRAGE_MSG_ROLLING_RESP);
            builder.setBytesData(resp.build().toByteString());

            ctx.writeAndFlush(builder);
        } catch (Exception e) {
            log.error("[Exception]-[BarrageMsgRollingServiceImpl]-[dealWithBarrageMessage]", e);
        }
    }
}
