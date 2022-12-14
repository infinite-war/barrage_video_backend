package top.houry.netty.barrage.service.impl;

import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.houry.netty.barrage.annotation.BarrageAnnotation;
import top.houry.netty.barrage.consts.BarrageMsgTypeConst;
import top.houry.netty.barrage.proto.BarrageProto;
import top.houry.netty.barrage.service.IBarrageMsgService;
import top.houry.netty.barrage.service.IBarrageMsgTypeService;
import top.houry.netty.barrage.service.IBarrageWatchInfoService;

/**
 * @Desc 心跳包信息
 * @Author houruiyang
 * @Date 2021/8/16
 **/
@Service
@BarrageAnnotation(msgType = BarrageMsgTypeConst.WEB_CLIENT_HEART_BEAT_REQ)
@Slf4j
public class BarrageHeartBeatMsgServiceImpl implements IBarrageMsgTypeService {

    private IBarrageMsgService msgService;

    private IBarrageWatchInfoService watchInfoService;

    @Autowired
    public void setBarrageWatchInfoService(IBarrageWatchInfoService barrageWatchInfoService) {
        this.watchInfoService = barrageWatchInfoService;
    }

    @Autowired
    public void setMsgService(IBarrageMsgService msgService) {
        this.msgService = msgService;
    }


    /**
     * 处理心跳逻辑
     *
     * @param barrage {@link BarrageProto}
     * @param ctx     通道上下文信息
     */
    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {
        try {
            BarrageProto.WebClientHeartBeatReq heartBeatReq = BarrageProto.WebClientHeartBeatReq.parseFrom(barrage.getBytesData());
            log.info("[Req]-[BarrageHeartBeatMsgServiceImpl]-[dealWithBarrageMessage]-[接收到心跳]-[ctx:{}]-[params:{}]", ctx.channel().toString(), TextFormat.printToUnicodeString(heartBeatReq));

            String videoId = StringUtils.isBlank(heartBeatReq.getVideoId()) ? "" : heartBeatReq.getVideoId();

            BarrageProto.Barrage.Builder builder = BarrageProto.Barrage.newBuilder();
            BarrageProto.WebClientHeartBeatResp.Builder resp = BarrageProto.WebClientHeartBeatResp.newBuilder();

            resp.setBarrageOnlineCount(Integer.parseInt(watchInfoService.getTotalOnlineWatchCount(videoId)));
            resp.setBarrageTotalCount(msgService.getMsgCountByVideoId(videoId));
            resp.setBarrageTotalWatchCount(Integer.parseInt(watchInfoService.getTotalWatchCount(videoId)));

            builder.setMsgType(BarrageMsgTypeConst.WEB_CLIENT_HEART_BEAT_RESP);
            builder.setBytesData(resp.build().toByteString());
            ctx.writeAndFlush(builder);
        } catch (Exception e) {
            log.error("[Exception]-[BarrageHeartBeatMsgServiceImpl]-[dealWithBarrageMessage]", e);
        }
    }
}
