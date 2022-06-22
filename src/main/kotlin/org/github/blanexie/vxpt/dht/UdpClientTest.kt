package org.github.blanexie.vxpt.dht

import cn.hutool.core.util.HexUtil
import cn.hutool.core.util.RandomUtil
import com.dampcake.bencode.Type
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import org.github.blanexie.vxpt.bencode
import java.net.InetSocketAddress


fun main() {
    val group: EventLoopGroup = NioEventLoopGroup()
    val b = Bootstrap()
    b.group(group)
        .channel(NioDatagramChannel::class.java)
        .option(ChannelOption.SO_BROADCAST, true)
        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .handler(UdpClientHandler())
    val ch = b.bind(7777).sync().channel()
    // 客户端等待10s用于接收服务端的应答消息，然后退出并释放资源

    val resp = mapOf(
        "t" to "aa",
        "y" to "q",
        "q" to "find_node",
        "a" to mapOf("id" to HexUtil.decodeHex(router.id), "target" to RandomUtil.randomBytes(20))
    )
    val bytes = bencode.encode(resp)
    println(String(bytes))

    val byteBuf = Unpooled.copiedBuffer(bytes)
    ch.writeAndFlush(
        DatagramPacket(
            byteBuf,
            InetSocketAddress("router.bittorrent.com", 6881)
        )
    ).sync()
    println("发送完成")
    ch.closeFuture().await()
}

class UdpClientHandler : SimpleChannelInboundHandler<DatagramPacket>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: DatagramPacket) {
        val byteBuf = msg.content()
        val bytes = ByteArray(byteBuf.readableBytes())
        val dictionary = bencode.decode(bytes, Type.DICTIONARY)
        println("receive server msg:" + dictionary)
    }
}
