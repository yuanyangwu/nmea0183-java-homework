package com.frankwu.nmea.datasource;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.Function;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.CodecManagerActor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

/**
 * Created by wuf2 on 5/1/2015.
 */
public class NettyTcpServerDataSourceActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private int port;
    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;

    private SupervisorStrategy supervisorStrategy = new OneForOneStrategy(3, Duration.create("5 seconds"), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
            if (param instanceof IllegalArgumentException) {
                return SupervisorStrategy.resume();
            }
            return SupervisorStrategy.escalate();
        }
    });

    public NettyTcpServerDataSourceActor(int port, CodecManager codecManager, String monitorAddress) {
        this.port = port;
        ActorRef codecManagerRef = getContext().actorOf(CodecManagerActor.props(codecManager, monitorAddress), "codecManager");
    }

    public static Props props(int port, CodecManager codecManager, String monitorAddress) {
        return Props.create(new Creator<NettyTcpServerDataSourceActor>() {
            @Override
            public NettyTcpServerDataSourceActor create() throws Exception {
                return new NettyTcpServerDataSourceActor(port, codecManager, monitorAddress);
            }
        });
    }

    @Override
    public void preStart() throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        final ActorSelection codecManagerActor = getContext().actorSelection("codecManager");
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new StringDecoder())
                                    .addLast(new NettyTcpServerHandler(codecManagerActor));
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        } catch (Exception e) {
            logger.error("Start TCP server fails {}", e);
        }
    }

    @Override
    public void postStop() throws Exception {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());
        unhandled(message);
    }

    @ChannelHandler.Sharable
    private static class NettyTcpServerHandler extends SimpleChannelInboundHandler<String> {
        private final Logger logger = LoggerFactory.getLogger(NettyTcpServerHandler.class);
        private final ActorSelection codecManagerActor;

        public NettyTcpServerHandler(ActorSelection codecManagerActor) {
            this.codecManagerActor = codecManagerActor;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            logger.info("{}", msg);
            codecManagerActor.tell(msg, ActorRef.noSender());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("", cause);
            ctx.close();
        }
    }
}
