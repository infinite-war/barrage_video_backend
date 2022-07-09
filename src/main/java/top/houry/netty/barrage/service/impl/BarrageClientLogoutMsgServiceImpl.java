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
import top.houry.netty.barrage.service.IBarrageMsgTypeService;
import top.houry.netty.barrage.service.IBarrageWatchInfoService;
import top.houry.netty.barrage.utils.BarrageConnectInfoUtils;

/**
 * @Desc
 * @Author houruiyang
 * @Date 2022/1/22
 **/
@Service
@BarrageAnnotation(msgType = BarrageMsgTypeConst.WEB_CLIENT_LOGOUT_REQ)
@Slf4j
public class BarrageClientLogoutMsgServiceImpl implements IBarrageMsgTypeService {

    private IBarrageWatchInfoService watchInfoService;

    @Autowired
    public void setWatchInfoService(IBarrageWatchInfoService watchInfoService) {
        this.watchInfoService = watchInfoService;
    }

    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {
        try {
            BarrageProto.WebClientLogoutReq logoutInfo = BarrageProto.WebClientLogoutReq.parseFrom(barrage.getBytesData());
            log.info("[Req]-[BarrageClientLogoutMsgServiceImpl]-[dealWithBarrageMessage]-[params{}]",  TextFormat.printToUnicodeString(logoutInfo));
            String videoId = StringUtils.isBlank(logoutInfo.getVideoId()) ? "" : logoutInfo.getVideoId();
            BarrageConnectInfoUtils.removeChannelInfoFromBaseMap(videoId, ctx);
            watchInfoService.subOnlineWatchCount(videoId);
        } catch (Exception e) {
            log.error("[Exception]-[BarrageClientLogoutMsgServiceImpl]-[dealWithBarrageMessage]", e);
        }
    }
}
