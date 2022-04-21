package org.github.blanexie.vxpt.post.model

import java.time.LocalDateTime
import javax.persistence.*

/**
 * 帖子,
 */
@Entity
@Table(schema = "vxpt")
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var title: String, // 标题
    var cover: String, //封面

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        schema = "vxpt", name = "post_labels",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [
            JoinColumn(name = "label_id")
        ]
    )
    var labels: Set<Label>,
    @Column(columnDefinition = "TEXT")
    var content: String,  //markdown 文本描述

    var userId: Int, //用户的id
    var status: Int,
    var createTime: LocalDateTime = LocalDateTime.now(),
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {


}

/**
 * 标签
 */
@Entity
@Table(schema = "vxpt")
class Label(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var type: String,  //标签分组, 比如: 码率标签,  分级标签, 来源标签当
    var name: String,  //具体标签名称
    var description: String? = null,
)
