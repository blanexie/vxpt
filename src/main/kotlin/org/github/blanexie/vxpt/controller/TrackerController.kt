package org.github.blanexie.vxpt.controller

import cn.hutool.core.util.HexUtil
import com.dampcake.bencode.Bencode
import io.netty.buffer.Unpooled
import org.github.blanexie.vxpt.tracker.model.Peer
import org.github.blanexie.vxpt.tracker.service.PeerService
import org.github.blanexie.vxpt.tracker.service.dto.PeerDTO
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.support.ModelAndViewContainer
import reactor.core.publisher.Mono


val bencode = Bencode(true)

@RestController
class TrackerController(val peerService: PeerService) {

    @GetMapping("/announce")
    fun uploadTorrent(peerDTO: PeerDTO, response: ServletServerHttpResponse): Mono<ByteArray> {
        val fromCallable = Mono.fromCallable {
            val peers = peerService.announce(peerDTO)
            val buildResp = this.buildResp(peers)
            return@fromCallable bencode.encode(buildResp)
        }
        return fromCallable
    }

    /**
     * 构建响应
     */
    fun buildResp(peers: List<Peer>): Map<String, Any> {
        val count = peers.count { it.event == "completed" }
        val resp = hashMapOf<String, Any>()
        resp["interval"] = 3600
        resp["min interval"] = 30
        resp["incomplete"] = peers.size - count
        resp["complete"] = count
        val bytebuf = Unpooled.buffer()
        peers
            .filter { it.ipAddr.ip != null }.filterNotNull()
            .map { getCompactPeer(it.ipAddr.ip!!.first(), it.ipAddr.port) }
            .forEach { bytes -> bytebuf.writeBytes(bytes) }
        val nioBuffer = bytebuf.nioBuffer()
        if (bytebuf.readableBytes() > 0) {
            resp["peers"] = nioBuffer
        }
        val bytebuf6 = Unpooled.buffer()
        peers
            .filter { it.ipAddr.ipv6 != null }.filterNotNull()
            .map {
                val list = it.ipAddr.ipv6
                list?.map { str -> getCompactPeer6(str, it.ipAddr.port) }
                    ?.reduce { acc, bytes -> acc.plus(bytes) }
            }
            .forEach { bytebuf6.writeBytes(it) }
        if (bytebuf6.readableBytes() > 0) {
            resp["peers6"] = bytebuf6.nioBuffer()
        }
        return resp
    }
}

private fun compactIpv6(ipv6: String): ByteArray {
    if (ipv6.startsWith("::")) {
        val split = ipv6.split("::")
        val s2 = split[1]
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - -decodeHex1.size
        return ByteArray(i).plus(decodeHex1)
    }
    if (ipv6.endsWith("::")) {
        val split = ipv6.split("::")
        val s2 = split[0]
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - -decodeHex1.size
        return decodeHex1.plus(ByteArray(i))
    }
    if (ipv6.contains("::")) {
        val split = ipv6.split("::")
        val s1 = split[0]
        val s2 = split[1]
        val decodeHex = HexUtil.decodeHex(s1)
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - decodeHex.size - decodeHex1.size
        return decodeHex.plus(ByteArray(i)).plus(decodeHex1)
    }
    val split = ipv6.split(":")
    return split.map { HexUtil.decodeHex(it) }.reduce { acc, bytes -> acc.plus(bytes) }
}

private fun getCompactPeer6(ipv6: String, port: Int): ByteArray {
    val ipv6Bytes = compactIpv6(ipv6)
    val p1 = port ushr 8
    val p2 = port and 255
    return ipv6Bytes.plus(p1.toByte()).plus(p2.toByte())
}

private fun getCompactPeer(ip: String, port: Int): ByteArray {
    val map = ip.split(".").map { it.toInt().toByte() }
    val p1 = port ushr 8
    val p2 = port and 255
    val plus = map.plus(p1.toByte()).plus(p2.toByte())
    return plus.toByteArray()
}
