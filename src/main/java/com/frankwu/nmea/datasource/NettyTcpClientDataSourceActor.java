package com.frankwu.nmea.datasource;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.Function;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.CodecManagerActor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;


/**
 * Created by wuf2 on 5/2/2015.
 */
public class NettyTcpClientDataSourceActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private String host;
    private int port;
    private boolean running = false;
    private Thread clientThread;

    private SupervisorStrategy supervisorStrategy = new OneForOneStrategy(3, Duration.create("5 seconds"), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
            if (param instanceof IllegalArgumentException) {
                return SupervisorStrategy.resume();
            }
            return SupervisorStrategy.escalate();
        }
    });

    public NettyTcpClientDataSourceActor(String host, int port, CodecManager codecManager) {
        this.host = host;
        this.port = port;
        ActorRef codecManagerRef = getContext().actorOf(CodecManagerActor.props(codecManager), "codecManager");
    }

    public static Props props(String host, int port, CodecManager codecManager) {
        return Props.create(new Creator<NettyTcpClientDataSourceActor>() {
            @Override
            public NettyTcpClientDataSourceActor create() throws Exception {
                return new NettyTcpClientDataSourceActor(host, port, codecManager);
            }
        });
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void preStart() throws Exception {
        logger.debug("NettyTcpClientDataSourceActor.preStart");
        clientThread = new ClientThread(this);
        running = true;
        clientThread.start();
    }

    @Override
    public void postStop() throws Exception {
        logger.debug("NettyTcpClientDataSourceActor.postStop");
        try {
            running = false;
            clientThread.interrupt();
            clientThread.join();
        } catch (InterruptedException e) {
            logger.error("shutdown NettyTcpClientDataSource fail: {}", e);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());
        unhandled(message);
    }

    private static class ClientThread extends Thread {
        private final Logger logger = LoggerFactory.getLogger(ClientThread.class);
        private NettyTcpClientDataSourceActor actor;

        public ClientThread(NettyTcpClientDataSourceActor actor) {
            super("NettyTcpClientDataSourceActor.ClientThread");
            this.actor = actor;
        }

        @Override
        public void run() {
            final ActorSelection codecManagerActor = actor.getContext().actorSelection("codecManager");
            while (actor.isRunning()) {
                logger.info("connecting {}:{}", actor.getHost(), actor.getPort());
                EventLoopGroup group = new NioEventLoopGroup();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new LoggingHandler(LogLevel.INFO))
                                            .addLast(new StringDecoder())
                                            .addLast(new NettyTcpClientHandler(codecManagerActor));
                                }
                            });
                    ChannelFuture channelFuture = bootstrap.connect(actor.getHost(), actor.getPort()).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    if (actor.isRunning()) {
                        logger.error("read fail", e);
                    } else {
                        logger.info("shutdown gracefully");
                    }
                } finally {
                    group.shutdownGracefully();
                }
            }
        }
    }

    private static class NettyTcpClientHandler extends SimpleChannelInboundHandler<String> {
        private final Logger logger = LoggerFactory.getLogger(NettyTcpClientHandler.class);
        private final ActorSelection codecManagerActor;

        public NettyTcpClientHandler(ActorSelection codecManagerActor) {
            this.codecManagerActor = codecManagerActor;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            codecManagerActor.tell(msg, ActorRef.noSender());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("", cause);
            ctx.close();
        }
    }
}
