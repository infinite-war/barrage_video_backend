package top.houry.netty.barrage.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.houry.netty.barrage.config.NettyConfigProperties;
import top.houry.netty.barrage.listener.NettyServerListener;

/**
 * @Desc netty 服务
 * @Author houry
 * @Date 2021/3/2 8:48
 **/
@Component
@Slf4j
public class NettyServer {


    private NettyConfigProperties nettyConfigProperties;

    @Autowired
    public void setNettyConfigProperties(NettyConfigProperties nettyConfigProperties) {
        this.nettyConfigProperties = nettyConfigProperties;
    }

    /**
     * socket 连接处理循环组
     */
    EventLoopGroup boss = new NioEventLoopGroup(1, new NettyThreadFactory("netty-screen-boss"));
    /**
     * socket 业务处理循环组
     */
    EventLoopGroup worker = new NioEventLoopGroup(1, new NettyThreadFactory("netty-screen-worker"));


    /**
     * 关闭Netty服务
     */
    public void shutDownNettyServer() {
        try {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("[NettyServer]-[shutDownNettyServer]-[shutDown]");
        } catch (Exception e) {
            log.error("[NettyServer]-[shutDownNettyServer]-[Exception]");
        }

    }

    /**
     * 开启Netty服务
     */
    public void startNettyServer() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new NettyServerInitializer());
            ChannelFuture channelFuture = server.bind(nettyConfigProperties.getServerPort()).addListener(new NettyServerListener(nettyConfigProperties.getServerPort())).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("[NettyServer]-[startNettyServer]-[Exception]", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("[NettyServer]-[startNettyServer]-[shutdownGracefully]");
        }

    }

}
