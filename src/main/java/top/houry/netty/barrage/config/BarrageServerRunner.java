package top.houry.netty.barrage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.houry.netty.barrage.netty.WebSocketNettyServer;
import top.houry.netty.barrage.service.IBarrageMsgSensitiveService;
import top.houry.netty.barrage.utils.BarrageMsgSensitiveUtils;

/**
 * @Desc 抽取公共的需要在SpringBoot加载完毕之后加载的业务
 * @Author houry
 * @Date 2021/3/2 8:48
 **/
@Component
public class BarrageServerRunner implements ApplicationRunner {


    private WebSocketNettyServer nettyServer;

    private IBarrageMsgSensitiveService barrageMsgSensitiveService;

    @Autowired
    public void setNettyServer(WebSocketNettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Autowired
    public void setBarrageMsgSensitiveService(IBarrageMsgSensitiveService barrageMsgSensitiveService) {
        this.barrageMsgSensitiveService = barrageMsgSensitiveService;
    }


    @Override
    public void run(ApplicationArguments args) {
        startNetty();
        initSensitiveMsg();
    }

    private void startNetty() {
        new Thread(() -> nettyServer.startNettyServer()).start();
    }

    private void initSensitiveMsg() {
        BarrageMsgSensitiveUtils.setSensitiveWords(barrageMsgSensitiveService.list());
    }

}
