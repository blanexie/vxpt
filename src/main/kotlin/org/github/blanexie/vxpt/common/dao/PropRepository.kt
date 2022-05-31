package org.github.blanexie.vxpt.common.dao

import org.github.blanexie.vxpt.common.model.Prop
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PropRepository : CrudRepository<Prop, Int> {
    fun findByKey(key: String): Prop
}
