package org.github.blanexie.vxpt.post.service

import org.github.blanexie.vxpt.post.dao.TorrentRepository
import org.github.blanexie.vxpt.post.model.Torrent
import org.springframework.stereotype.Service

@Service
class TorrentService(val torrentRepository: TorrentRepository) {

    fun save(torrent: Torrent): Torrent {
        return torrent.save(torrentRepository)
    }

}