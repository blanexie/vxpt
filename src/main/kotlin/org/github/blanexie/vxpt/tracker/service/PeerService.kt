package org.github.blanexie.vxpt.tracker.service

import org.github.blanexie.vxpt.support.AuthUtil
import org.github.blanexie.vxpt.tracker.dao.PeerRepository
import org.github.blanexie.vxpt.tracker.model.Peer
import org.github.blanexie.vxpt.tracker.service.dto.PeerDTO
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

/**
 *
 * @author ：xiezc
 * @date   ：2022/4/29 2:52 PM
 */
@Service
class PeerService(
    val authUtil: AuthUtil,
    val peerRepository: PeerRepository,
    val eventPublisher: ApplicationEventPublisher
) {

    fun announce(peerDTO: PeerDTO): List<Peer> {
        val peer = peerRepository.findByAuthKeyAndInfoHashAndStatus(peerDTO.authKey, peerDTO.infoHash, 0)
        return peer?.let {
            //更新数据
            //发出消息
            //获取其他peer
            it.findPeers(peerRepository)
        } ?: listOf<Peer>()
    }

    fun scrape() {

    }

}