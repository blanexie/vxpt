package org.github.blanexie.vxpt.support.event

import org.springframework.context.ApplicationEvent


/**
 * 种子下载事件
 */
class TorrentDownloadEvent(
    auth: String, infoHash: String,
    userId: Int,
    torrentId: Int
) : ApplicationEvent(
    mapOf(
        "auth" to auth,
        "infoHash" to infoHash, "userId" to userId, "torrentId" to torrentId
    )
) {
    fun getInfoHash(): String {
        val map = this.source as Map<String, *>
        return map["infoHash"] as String
    }

    fun getTorrentId(): String {
        val map = this.source as Map<String, *>
        return map["torrentId"] as String
    }

    fun getUserId(): String {
        val map = this.source as Map<String, *>
        return map["userId"] as String
    }

    fun getAuth(): String {
        val map = this.source as Map<String, *>
        return map["auth"] as String
    }

}
