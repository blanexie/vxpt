package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.post.dto.TorrentDTO
import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.post.service.TorrentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/torrent")
class TorrentController(val torrentService: TorrentService) {


    @GetMapping("download")
    fun download(@RequestParam infoHash: String) {



    }


}