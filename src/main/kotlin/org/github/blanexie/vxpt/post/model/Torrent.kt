package org.github.blanexie.vxpt.post.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.github.blanexie.vxpt.post.dao.TorrentRepository
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import javax.persistence.*

/**
 *
 * 种子
 */
@Entity
@Table(schema = "vxpt")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class Torrent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    var postId: Int,// 帖子id
    var name: String,

    @Column(name = "`size`")
    var size: Long, //  总大小
    @Embedded
    var ration: Ration,

    val comment: String,//：种子文件的注释
    val creationDate: Long,//：种子文件建立的时间，是从1970年1月1日00:00:00到现在的秒数。
    val encoding: String,  //：种子文件的默认编码，比如GB2312，Big5，utf-8等

    @Column(unique = true)
    var infoHash: String,

    val infoByte: ByteArray,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var infoFiles: Set<FileInfo>?,
    val infoName: String,

    val status: Int = 0,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val updateTime: LocalDateTime = LocalDateTime.now(),
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val createTime: LocalDateTime = LocalDateTime.now(),
) {

    fun save(torrentRepository: TorrentRepository): Torrent {
        return torrentRepository.save(this)
    }

}

/**
 * 种子中的info信息
 */
data class FileInfo(
    var length: Long,
    var path: List<String>,
)

@Embeddable
class Announce(var announce: String, var announceList: String?)

/* 倍率 */
@Embeddable
class Ration(
    var rate: Float,
    @Column(name = "rate_start")
    var start: LocalDateTime,
    @Column(name = "rate_end")
    var end: LocalDateTime
)

@Embeddable
class Pieces(
    var pieces: String,
    @Column(name = "pieces_length")
    var length: Long
)

@Embeddable
class Publisher(
    var publisher: String?,
    var publisherUrl: String?,
)

