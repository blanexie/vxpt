package org.github.blanexie.vxpt.post.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 资源
 */
@Entity
@Table(schema = "vxpt")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class Resource(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var type: String, // torrent,img, audio,
    val path: String, // 文件的存放地址
    val md5: String, // 文件的md5
    val name: String, // 文件名称
    val size: Long, //文件的大小
    val suffix: String, //文件的后缀

    val userId: Int, // 资源上传者

    var status: Int = 0,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime = LocalDateTime.now(),
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {
}