package org.github.blanexie.vxpt.dht

import cn.hutool.core.util.HexUtil
import cn.hutool.core.util.RandomUtil
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioDatagramChannel

val router= Router(HexUtil.encodeHexStr(RandomUtil.randomBytes(20)), HashMap())
class NioDataChannelInitializer : ChannelInitializer<NioDatagramChannel>() {
    override fun initChannel(ch: NioDatagramChannel) {
        ch.pipeline().addLast("server", UdpChannelInboundHandler(router))
    }
}