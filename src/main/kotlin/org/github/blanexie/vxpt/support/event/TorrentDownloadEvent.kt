package org.github.blanexie.vxpt.support.event

import org.springframework.context.ApplicationEvent


/**
 * 种子下载事件
 */
class TorrentDownloadEvent(
    val auth: String,
    val infoHash: String,
    val userId: Int,
    val torrentId: Int
) : ApplicationEvent(
    mapOf(
        "auth" to auth,
        "infoHash" to infoHash, "userId" to userId, "torrentId" to torrentId
    )
)