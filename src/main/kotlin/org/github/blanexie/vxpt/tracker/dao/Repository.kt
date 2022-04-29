package org.github.blanexie.vxpt.tracker.dao

import org.github.blanexie.vxpt.post.model.Label
import org.github.blanexie.vxpt.tracker.model.Peer
import org.springframework.data.repository.CrudRepository

/**
 *
 * @author ：xiezc
 * @date   ：2022/4/29 2:40 PM
 */
interface PeerRepository : CrudRepository<Peer, Int> {

    fun findAllByInfoHashAndUserIdNotInAndStatus(infoHash: String, userIds: Set<Int>, status: Int): List<Peer>

    fun findByAuthKeyAndInfoHashAndStatus(authKey: String, infoHash: String, status: Int): Peer?


}