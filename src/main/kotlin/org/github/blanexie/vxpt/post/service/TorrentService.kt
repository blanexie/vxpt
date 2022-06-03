package org.github.blanexie.vxpt.post.service

import cn.hutool.core.date.DateUtil
import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Type
import org.github.blanexie.vxpt.bencode
import org.github.blanexie.vxpt.common.service.PropService
import org.github.blanexie.vxpt.post.dao.TorrentRepository
import org.github.blanexie.vxpt.post.dto.TorrentDTO
import org.github.blanexie.vxpt.post.model.FileInfo
import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.support.event.TorrentDownloadEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class TorrentService() {

    val announceKeyPrefix = "announceKeyPrefix";

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Autowired
    lateinit var torrentRepository: TorrentRepository

    @Autowired
    lateinit var propService: PropService

    fun save(torrentDTO: TorrentDTO, postId: Int, userId: Int): Torrent {
        val infoByte = torrentDTO.infoByte
        val infoHash = DigestUtil.md5Hex(infoByte)

        val infoMap = bencode.decode(infoByte, Type.DICTIONARY)
        val files = infoMap["files"]?.let { it as List<*> }
            ?.map { it as Map<String, *> }
            ?.map { FileInfo(it["length"] as Long, it["path"] as List<String>) }
            ?.toSet()

        val size = files?.map { it.length }?.sum()
        val infoName = infoMap["name"] as String
        val torrent = Torrent(
            null, postId, torrentDTO.name, size!!, torrentDTO.ration, torrentDTO.comment,
            torrentDTO.creationDate, torrentDTO.encoding, infoHash, infoByte, files, infoName
        )
        torrent.save(torrentRepository)
        return torrent
    }

    fun download(infoHash: String, userId: Int): ByteArray {
        val prop = propService.getByKey(announceKeyPrefix)
        val torrent = torrentRepository.findByInfoHash(infoHash)
        val infoByte = torrent.infoByte
        val torrentMap = hashMapOf<String, Any>()
        torrentMap["info"] = infoByte
        torrentMap["encoding"] = "utf-8"
        val creationDate = DateUtil.date(torrent.creationDate).toString("yyyy-MM-dd HH:mm:ss")
        torrentMap["creation date"] = creationDate

        val auth = DigestUtil.md5Hex("${torrent.id}${torrent.infoHash}${userId}")
        
        torrentMap["announce"] = prop.value + auth
        torrentMap["comment"] = "vxpt auth:${auth}"
        val encode = bencode.encode(torrentMap)
        val torrentDownloadEvent = TorrentDownloadEvent(auth, torrent.infoHash, userId, torrent.id!!)
        applicationEventPublisher.publishEvent(torrentDownloadEvent)
        return encode
    }


}