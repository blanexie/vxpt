package org.github.blanexie.vxpt.post.dto

import org.github.blanexie.vxpt.post.model.Label


/**
 * 帖子搜索对象
 */
class PostSearchQuery(
    val title: String,
    val labels: Set<Label>,

    val pageNo: Int = 1,
    val pageSize: Int = 25,
)