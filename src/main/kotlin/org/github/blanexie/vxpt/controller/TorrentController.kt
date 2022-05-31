package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.account.model.User
import org.github.blanexie.vxpt.post.dto.TorrentDTO
import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.post.service.TorrentService
import org.github.blanexie.vxpt.support.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/torrent")
class TorrentController(val torrentService: TorrentService) {

    @GetMapping("download")
    fun download(@SessionAttribute("user") user: User, @RequestParam infoHash: String): Mono<WebResponse> {
        return Mono.fromCallable {
            val torrent = torrentService.download(infoHash, user.id!!)
            WebResponse(data = torrent)
        }
    }

}