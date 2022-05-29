package org.github.blanexie.vxpt.controller

import com.dampcake.bencode.Bencode
import org.github.blanexie.vxpt.account.model.User
import org.github.blanexie.vxpt.post.dto.PostDTO
import org.github.blanexie.vxpt.post.dto.PostSearchQuery
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Post
import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.post.service.PostService
import org.github.blanexie.vxpt.post.service.TorrentService
import org.github.blanexie.vxpt.support.WebResponse
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/post")
class PostController(val postService: PostService, val torrentService: TorrentService) {

    @PostMapping("/save")
    fun savePost(@RequestBody postDTO: PostDTO, @SessionAttribute("user") user: User): Mono<WebResponse> {
        return Mono.fromCallable {
            val post = postService.savePost(postDTO, user.id!!)
            postDTO.torrents.forEach {
                torrentService.save(it, post.id!!, user.id!!)
            }
            WebResponse()
        }
    }

    /**
     * 根据标签搜索内容
     */
    @PostMapping("/search")
    fun search(@RequestBody postSearchQuery: PostSearchQuery): WebResponse {
        val search = postService.search(postSearchQuery)
        return WebResponse(data = search)
    }

}
