package top.houry.netty.barrage.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import top.houry.netty.barrage.consts.BarrageWebSocketConst;
import top.houry.netty.barrage.proto.BarrageProto;

import java.util.concurrent.TimeUnit;

/**
 * @Desc 配置netty pipeline
 * @Author houry
 * @Date 2021/3/2 9:22
 **/
public class WebSocketNettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());   //http编码器
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(BarrageWebSocketConst.MAX_CONTENT_LENGTH));    //http包接收聚合
        pipeline.addLast(new IdleStateHandler(BarrageWebSocketConst.READER_IDLE_TIME, BarrageWebSocketConst.WRITE_IDLE_TIME, BarrageWebSocketConst.ALL_IDLE_TIM, TimeUnit.SECONDS));
        pipeline.addLast(new WebSocketServerProtocolHandler(BarrageWebSocketConst.WEB_SOCKET_PATH));   //ws请求路径
        pipeline.addLast(new WebSocketServerCompressionHandler());  //WebSocket 服务器压缩处理程序
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new WebSocketNettyServerMessageDecoder());   //websocket服务端解码器
        pipeline.addLast(new ProtobufDecoder(BarrageProto.Barrage.getDefaultInstance()));   //解码客户端传来的proto对象
        pipeline.addLast(new WebSocketNettyServerMessageEncoder());   //websocket服务端编码器
        pipeline.addLast(new ProtobufEncoder());     //编码proto对象
        pipeline.addLast(new WebSocketNettyServerHandler());    //WebSocket Netty 服务器处理程序

    }
}
