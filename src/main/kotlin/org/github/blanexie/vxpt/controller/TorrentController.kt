package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.post.service.TorrentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/torrent")
class TorrentController(val torrentService: TorrentService) {

    @PostMapping("upload")
    fun uploadTorrent(@RequestBody torrent: Torrent) {
        torrentService.save(torrent)
    }


}