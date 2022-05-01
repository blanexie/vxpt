package org.github.blanexie.vxpt.post.service

import org.github.blanexie.vxpt.post.dao.PostRepository
import org.github.blanexie.vxpt.post.dto.PostSearchQuery
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PostService(val postRepository: PostRepository) {

    fun savePost(post: Post): Post {
        return post.save(postRepository)
    }

    fun findById(id: Int): Post {
        return postRepository.findById(id).orElse(null)
    }

    fun search(postSearchQuery: PostSearchQuery): Page<Post> {
        val page = postRepository.findByTitleLikeAndLabelsInAndStatus(
            postSearchQuery.title,
            postSearchQuery.labels,
            1,
            PageRequest.of(postSearchQuery.pageNo, postSearchQuery.pageSize)
        )
        return page
    }


}