package org.github.blanexie.vxpt.support.event

import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

/**
 * peer 上报事件
 */
class PeerAnnounceEvent(
    val infoHash: String,
    val downloaded: Long,
    val uploaded: Long,
    val left: Long,
    val event: String,
    val announceTime: LocalDateTime= LocalDateTime.now(),
) : ApplicationEvent(
    mapOf(
        "infoHash" to infoHash,
        "downloaded" to downloaded,
        "uploaded" to uploaded,
        "left" to left,
        "event" to event,
        "announceTime" to announceTime

    )
) {


}