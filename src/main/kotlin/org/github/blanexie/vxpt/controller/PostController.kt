package org.github.blanexie.vxpt.controller

import com.dampcake.bencode.Bencode
import org.github.blanexie.vxpt.post.dto.PostSearchQuery
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Post
import org.github.blanexie.vxpt.post.model.Torrent
import org.github.blanexie.vxpt.post.service.PostService
import org.github.blanexie.vxpt.support.WebResponse
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/post")
class PostController(val postService: PostService) {

    @PostMapping("/save")
    fun savePost(@RequestBody post: Post): WebResponse {
        return WebResponse(data = post.save(postService.postRepository))
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
