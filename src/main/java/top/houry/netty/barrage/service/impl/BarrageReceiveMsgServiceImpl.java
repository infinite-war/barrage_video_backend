package top.houry.netty.barrage.service.impl;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.houry.netty.barrage.annotation.BarrageAnnotation;
import top.houry.netty.barrage.proto.BarrageProto;
import top.houry.netty.barrage.service.BarrageMsgTypeService;

/**
 * @Desc 服务器接收客户端发送过来的弹幕信息
 * @Author houruiyang
 * @Date 2022/1/18
 **/
@Service
@BarrageAnnotation(msgType = "server.receive")
@Slf4j
public class BarrageReceiveMsgServiceImpl implements BarrageMsgTypeService {
    @Override
    public void dealWithBarrageMessage(BarrageProto.Barrage barrage, ChannelHandlerContext ctx) {

    }
}
