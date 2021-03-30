package com.tiza.leo.UdpGetwayForwardByNetty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;


/**
 * @author leowei
 * @date 2021/3/30  - 23:01
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class ReceiveHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private Environment env;

    /*
    1. 从 channel  中取到msg  转换为buf 后 截取前9位扔掉
    2. 将剩余的数据打包为一个DatagramPacket 发往 出口
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket datagramPacketIn =   (DatagramPacket) msg;
        ByteBuf buf = datagramPacketIn.copy().content();
        buf.skipBytes(3);
        InetSocketAddress address = new InetSocketAddress(env.getProperty("gateway.outIp"), Integer.parseInt(env.getProperty("gateway.outPort")));
        DatagramPacket datagramPacketOut = new DatagramPacket(buf, address);
        ctx.channel().writeAndFlush(datagramPacketOut).sync();
        datagramPacketIn.release();
        //super.channelRead(ctx, msg);
    }
}
