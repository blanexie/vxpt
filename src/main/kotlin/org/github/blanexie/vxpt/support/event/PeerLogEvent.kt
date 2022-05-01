package org.github.blanexie.vxpt.support.event

import org.github.blanexie.vxpt.support.PeerLog
import org.springframework.context.ApplicationEvent

class PeerLogEvent(peerLog: PeerLog) : ApplicationEvent(peerLog) {

}
