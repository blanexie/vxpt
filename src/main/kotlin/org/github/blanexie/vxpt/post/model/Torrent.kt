package org.github.blanexie.vxpt.post.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 种子
 */
@Entity
@Table(schema = "vxpt")
class Torrent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var postId: Int,// 帖子id
    var name: String,
    @Column(name = "`size`")
    var size: Long, // 种子的总大小
    @Embedded
    var ration: Ration,
    @Embedded
    val announce: Announce,  //： Tracker的服务器
    val comment: String,//：种子文件的注释
    val creationDate: Long,//：种子文件建立的时间，是从1970年1月1日00:00:00到现在的秒数。
    val encoding: String,  //：种子文件的默认编码，比如GB2312，Big5，utf-8等
    @Column(unique = true)
    var infoHash: String,
    var infoLength: Long?,  //：文件的总大小（Byte）

    @OneToMany(mappedBy = "torrent")
    var infoFiles: Set<FileInfo>?, //多文件中的文件   //files 和 length 只能有一个


    val infoName: String, //：推荐的文件夹名，此项可于下载时更改
    @Embedded
    val infoPieces: Pieces, //：文件的特征信息（将所有文件按照piece length的字节大小分成块，每块计算一个SHA1值，然后将这些值连接起来所组成）
    @Embedded
    val infoPublisher: Publisher,

    val status: Int = 0,
    val updateTime: LocalDateTime = LocalDateTime.now(),
    val createTime: LocalDateTime = LocalDateTime.now(),
)

/**
 * 种子中的info信息
 */

@Table(schema = "vxpt")
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class FileInfo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var length: Long,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var path: List<String>,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var pathUtf8: List<String>?,//文件名的UTF-8编码，同上

    @ManyToOne
    var torrent: Torrent, //多文件中的文件   //files 和 length 只能有一个

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

