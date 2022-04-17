package org.github.blanexie.vxpt.post.dao

import org.github.blanexie.vxpt.post.model.FileInfo
import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.post.model.Post
import org.github.blanexie.vxpt.post.model.Torrent
import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Int> {

}

interface LabelRepository : CrudRepository<Label, Int> {
}

interface TorrentRepository : CrudRepository<Torrent, Int> {
}

interface FileInfoRepository : CrudRepository<FileInfo, Int> {
}

