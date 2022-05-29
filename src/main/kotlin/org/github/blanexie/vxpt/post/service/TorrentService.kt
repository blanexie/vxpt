package org.github.blanexie.vxpt.post.service

import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Type
import org.github.blanexie.vxpt.bencode
import org.github.blanexie.vxpt.post.dao.TorrentRepository
import org.github.blanexie.vxpt.post.dto.TorrentDTO
import org.github.blanexie.vxpt.post.model.FileInfo
import org.github.blanexie.vxpt.post.model.Torrent
import org.springframework.stereotype.Service

@Service
class TorrentService(val torrentRepository: TorrentRepository) {

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


    fun download(infoHash: String, userId: Int) {
        val torrent = torrentRepository.findByInfoHash(infoHash)
        val infoByte = torrent.infoByte


    }


}