package com.github.blanexie.vxpt.bbs.post.service


import cn.hutool.core.bean.BeanUtil
import com.github.blanexie.vxpt.api.bbs.dto.PostDTO
import com.github.blanexie.vxpt.bbs.post.entity.LabelDO
import com.github.blanexie.vxpt.bbs.post.entity.PostDO
import com.github.blanexie.vxpt.bbs.post.repository.LabelRepository
import com.github.blanexie.vxpt.bbs.post.repository.PostRepository
import com.github.blanexie.vxpt.bbs.util.RespCode
import com.github.blanexie.vxpt.bbs.util.VxptException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService(val postRepository: PostRepository, val labelRepository: LabelRepository) {

    fun findById(id: Int): PostDTO? {
        val postDO = postRepository.findById(id)
        if (postDO.isPresent) {
            return BeanUtil.copyProperties(postDO.get(), PostDTO::class.java)
        }
        return null
    }

    fun publish(id: Int, userId: Int) {
        val option = postRepository.findById(id)
        val postDO = option.orElseThrow {
            VxptException(RespCode.PARAM_ERROR, "请传入正确的帖子id")
        }
        postDO.publish()
        postRepository.save(postDO)
    }

    fun savePost(postDTO: PostDTO): Int {
        var postDO = PostDO(
            postDTO.id, postDTO.title, postDTO.cover, postDTO.category, postDTO.content,
            postDTO.userId, 0, LocalDateTime.now(), LocalDateTime.now()
        )
        postDO = postRepository.save(postDO)
        val labels = postDTO.labels
        labels.forEach {
            var labelDO = labelRepository.findByLabelAndPostId(it, postDO.id)
            if (labelDO == null) {
                labelDO = LabelDO(
                    null,
                    postDO.id,
                    it,
                    0,
                    LocalDateTime.now(),
                    LocalDateTime.now()
                )
                labelRepository.save(labelDO)
            }
        }
        return postDO.id
    }


}