package org.github.blanexie.vxpt.post.dao

import org.github.blanexie.vxpt.post.model.*
import org.springframework.data.domain.Page
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import  org.springframework.data.domain.Pageable

@Repository
interface PostRepository : CrudRepository<Post, Int> {

    fun findByTitleLikeAndLabelsInAndStatus(
        title: String,
        labels: Set<Label>,
        status: Int,
        pageable: Pageable
    ): Page<Post>

}

@Repository
interface LabelRepository : CrudRepository<Label, Int> {
}

@Repository
interface TorrentRepository : CrudRepository<Torrent, Int> {

    fun findByInfoHash(infoHash: String): Torrent

}

@Repository
interface ResourceRepository : CrudRepository<Resource, Int> {

    fun findByMd5(md5: String): Resource?

}

