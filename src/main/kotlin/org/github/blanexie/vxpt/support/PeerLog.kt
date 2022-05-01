package org.github.blanexie.vxpt.support


class PeerLog(
    var infoHash: String,
    var userId: Int,
    var downloaded: Long,
    var uploaded: Long,
    var left: Long,

    val attribute: Map<String, Any>
)