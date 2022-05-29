package org.github.blanexie.vxpt.post.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.github.blanexie.vxpt.post.dao.PostRepository
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

    var category: String, //分类

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        schema = "vxpt", name = "post_labels",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [
            JoinColumn(name = "label_id")
        ]
    )
    var labels: MutableSet<Label>,

    @Column(columnDefinition = "TEXT")
    var content: String,  //markdown 文本描述

    var userId: Int, //用户的id

    var status: Int = 0,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime = LocalDateTime.now(),
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {


    fun save(repository: PostRepository): Post {
        repository.save(this)
        return this
    }

    fun addLabel(repository: PostRepository, label: Label) {
        this.labels.add(label)
        repository.save(this)
    }

    fun deleteLabel(repository: PostRepository, label: Label) {
        this.labels.removeIf {
            it.id == label.id
        }
        repository.save(this)
    }


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
