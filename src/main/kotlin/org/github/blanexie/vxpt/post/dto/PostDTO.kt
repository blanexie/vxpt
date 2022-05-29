package org.github.blanexie.vxpt.post.dto

import org.github.blanexie.vxpt.post.model.Announce
import org.github.blanexie.vxpt.post.model.FileInfo
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Ration
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.*

class PostDTO(
    var title: String,             // 标题
    var cover: String,             //封面
    var category: String,          //分类
    var labels: MutableSet<Label>,
    var content: String,           //markdown 文本描述
    var torrents: List<TorrentDTO>

)


class TorrentDTO(
    var name: String,
    var ration: Ration,

    val comment: String,         //：种子文件的注释
    val creationDate: Long,      //：种子文件建立的时间，是从1970年1月1日00:00:00到现在的秒数。
    val encoding: String,        //：种子文件的默认编码，比如GB2312，Big5，utf-8等

    val infoByte: ByteArray,     //种子文件的二进制内容
)