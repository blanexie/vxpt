package org.github.blanexie.vxpt.dht

import cn.hutool.core.util.HexUtil
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.PooledByteBufAllocator
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import org.github.blanexie.vxpt.bencode
import java.net.DatagramPacket
import java.net.InetSocketAddress


class UdpServer {

    lateinit var channel: Channel

    fun run() {
        val nioEventLoopGroup = NioEventLoopGroup()
        val bootstrap = Bootstrap()
        bootstrap.group(nioEventLoopGroup)
            .channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_BROADCAST, true) // 广播模式
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // 线程池复用缓冲区
            .handler(NioDataChannelInitializer())
        channel = bootstrap.bind(8888).sync().channel()
        // 这里阻塞
        channel.closeFuture().await();
    }

    fun sendMsg() {
        val resp = mapOf(
            "t" to "aa", "y" to "q", "q" to "find_node", "a" to mapOf(
                "id" to HexUtil.decodeHex(router.id), "target" to "mnopqrstuvw1yz123456".toByteArray()
            )
        )

        val encode = bencode.encode(resp)
        val datagramPacket = DatagramPacket(encode,  encode.size, InetSocketAddress("192.168.124.8", 56664))
        channel!!.writeAndFlush(datagramPacket).sync()
    }

}

fun main() {
    val udpServer = UdpServer()
    udpServer.run()
    udpServer.sendMsg()

}