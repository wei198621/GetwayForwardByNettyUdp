package com.tiza.leo.UdpGetwayForwardByNetty.deamon;

import com.tiza.leo.UdpGetwayForwardByNetty.handler.ReceiveHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author leowei
 * @date 2021/3/30  - 23:02
 */
@Slf4j
@Service
public class GatewayDeamon {
    @Autowired
    private Environment env;
    @Autowired
    private ReceiveHandler receiveHandler;

    public void start(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Integer port = env.getRequiredProperty("gateway.inPort", Integer.class);
        log.info("绑定udp端口{}",port);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    //设置了UDP socket读缓冲区为2M
                    .option(ChannelOption.SO_RCVBUF, 1024 * 2048)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast("receive", receiveHandler);
                        }
                    });
            ChannelFuture future = null;
            future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

}
