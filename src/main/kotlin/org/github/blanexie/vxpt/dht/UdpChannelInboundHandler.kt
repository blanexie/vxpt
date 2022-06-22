package org.github.blanexie.vxpt.dht

import cn.hutool.core.util.HexUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.StrUtil
import com.dampcake.bencode.Type
import io.netty.buffer.UnpooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.github.blanexie.vxpt.bencode
import java.net.DatagramPacket
import java.net.SocketAddress
import java.nio.ByteBuffer

class UdpChannelInboundHandler(val router: Router) : SimpleChannelInboundHandler<DatagramPacket>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: DatagramPacket?) {
        val dictionary = bencode.decode(msg!!.data, Type.DICTIONARY)
        val t = dictionary["t"]
        val y = dictionary["y"]
        val q = dictionary["q"]
        val r = dictionary["r"]
        val a = dictionary["a"]
        println(dictionary)
        //收到请求
        if (q != null) {
            val qB = q as ByteBuffer
            val req = String(qB.array())
            if (req == "ping") {
                val resp = mapOf("t" to "aa", "y" to "r", "r" to mapOf("id" to HexUtil.decodeHex(router.id)))
                val encode = bencode.encode(resp)
                val datagramPacket = DatagramPacket(encode, encode.size, msg.socketAddress)
                ctx!!.writeAndFlush(datagramPacket)
                return
            }

            if (req == "find_node") {
                val a = a as Map<String, *>
                val id = a["id"] as ByteBuffer
                val reqNode = Node(HexUtil.encodeHexStr(id.array()), null, null, System.currentTimeMillis())
                router.nodes[reqNode.id] = reqNode
                val target = a["target"] as ByteBuffer
                val node = router.nodes[HexUtil.encodeHexStr(target.array())]
                if (node == null) {
                    val buffer = UnpooledByteBufAllocator(true).buffer()
                    for (node in router.nodes) {
                        val writeBytes = buffer.writeBytes(HexUtil.decodeHex(node.key))
                        if (writeBytes.readableBytes() >= 400) {
                            break
                        }
                    }
                    val resp = mapOf(
                        "t" to "aa", "y" to "r", "r" to mapOf(
                            "id" to HexUtil.decodeHex(router.id), "nodes" to buffer.array()
                        )
                    )
                    val encode = bencode.encode(resp)
                    val datagramPacket = DatagramPacket(encode, encode.size, msg.socketAddress)
                    ctx!!.writeAndFlush(datagramPacket)
                    return
                }
            }
            if (req == "get_peers") {
                val token = RandomUtil.randomString(8)
                val a = a as Map<String, *>
                val id = HexUtil.encodeHexStr((a["id"] as ByteBuffer).array())
                router.nodes.computeIfAbsent(id) {
                    Node(id, token, null, System.currentTimeMillis())
                }.let {
                    it.token = token
                    it.updateTime = System.currentTimeMillis()
                }

                val buffer = UnpooledByteBufAllocator(true).buffer()
                for (node in router.nodes) {
                    val writeBytes = buffer.writeBytes(HexUtil.decodeHex(node.key))
                    if (writeBytes.readableBytes() >= 400) {
                        break
                    }
                }
                val resp = mapOf(
                    "t" to "aa", "y" to "r", "r" to mapOf(
                        "id" to HexUtil.decodeHex(router.id), "nodes" to buffer.array(),
                        "token" to token
                    )
                )
                val encode = bencode.encode(resp)
                val datagramPacket = DatagramPacket(encode, encode.size, msg.socketAddress)
                ctx!!.writeAndFlush(datagramPacket)
                return
            }
            if (req == "announce_peer") {
                val a = a as Map<String, *>
                val id = HexUtil.encodeHexStr((a["id"] as ByteBuffer).array())
                val token = a["token"]
                val node = router.nodes[id]
                if (node != null && node.token == token) {
                    val resp = mapOf("t" to "aa", "y" to "r", "r" to mapOf("id" to HexUtil.decodeHex(router.id)))
                    val encode = bencode.encode(resp)
                    val datagramPacket = DatagramPacket(encode, encode.size, msg.socketAddress)
                    ctx!!.writeAndFlush(datagramPacket)
                    return
                }
            }
        }

        //收到响应
        if (r != null) {
            val r = r as Map<String, *>
            val id = HexUtil.encodeHexStr((r["id"] as ByteBuffer).array())
            router.nodes[id]?.let {
                if (it.q == "ping") {
                    it.updateTime = System.currentTimeMillis()
                    it.q = null
                }
                if (it.q == "find_node") {
                    it.updateTime = System.currentTimeMillis()
                    val nodes = r["nodes"] as ByteBuffer
                    val byteArray = ByteArray(4)
                    while (nodes.position() < nodes.limit()) {
                        val node = Node(
                            HexUtil.encodeHexStr(nodes.get(byteArray).array()),
                            null,
                            null,
                            System.currentTimeMillis()
                        )
                        router.nodes[node.id] = node
                    }
                }
                if (it.q == "get_peers") {

                }
            }
        }

    }

}
