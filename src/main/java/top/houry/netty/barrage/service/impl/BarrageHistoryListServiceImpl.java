package top.houry.netty.barrage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
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
 * @Date 2022/2/7
 **/
@Service
@BarrageAnnotation(msgType = BarrageMsgTypeConst.WEB_CLIENT_BARRAGE_HISTORY_REQ)
@Slf4j
public class BarrageHistoryListServiceImpl implements IBarrageMsgTypeService {

    private IBarrageMsgService barrageMsgService;

    @Autowired
    public void setBarrageMsgService(IBarrageMsgService barrageMsgService) {
        this.barrageMsgService = barrageMsgService;
    }

    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {
        try {
            BarrageProto.WebClientBarrageHistoryListReq barrageHistory = BarrageProto.WebClientBarrageHistoryListReq.parseFrom(barrage.getBytesData());
            log.info("[Req]-[BarrageHistoryListServiceImpl]-[dealWithBarrageMessage]-[params{}]",  TextFormat.printToUnicodeString(barrageHistory));
            String videoId = barrageHistory.getVideoId();
            List<BarrageMsg> barrageMsgList = barrageMsgService.getListByVideoId(videoId);

            List<BarrageProto.BarrageHistoryMessage> msgList = new ArrayList<>();
            barrageMsgList.forEach(v ->{
                BarrageProto.BarrageHistoryMessage.Builder message = BarrageProto.BarrageHistoryMessage.newBuilder();
                message.setMsg(v.getMsgColor());
                message.setCreateTime(BarrageDateUtils.dateToString(v.getCreateTime(), BarrageDateUtils.DateType.PURE_DATE_MD_HM_PATTERN));
                message.setMsgColor(v.getMsgColor());
                message.setSendTime(BarrageDateUtils.secondToNormTime(ObjectUtil.defaultIfNull(v.getVideoTime(), 0)));
                message.setMsg(v.getMsgContent());
                msgList.add(message.build());
            });

            BarrageProto.Barrage.Builder builder = BarrageProto.Barrage.newBuilder();
            BarrageProto.WebClientBarrageHistoryListResp.Builder resp = BarrageProto.WebClientBarrageHistoryListResp.newBuilder();
            resp.addAllList(msgList);
            builder.setMsgType(BarrageMsgTypeConst.WEB_CLIENT_BARRAGE_HISTORY_RESP);
            builder.setBytesData(resp.build().toByteString());

            ctx.writeAndFlush(builder);
        } catch (Exception e) {
            log.error("[Exception]-[BarrageHistoryListServiceImpl]-[dealWithBarrageMessage]", e);
        }
    }
}
