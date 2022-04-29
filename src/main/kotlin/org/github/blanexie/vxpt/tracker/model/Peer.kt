package org.github.blanexie.vxpt.tracker.model

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.github.blanexie.vxpt.post.model.Ration
import org.github.blanexie.vxpt.tracker.dao.PeerRepository
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import javax.persistence.*

/**
 *
///announce
// ?auth_key=e934b04ba3234d639c0523f0cb15fe0q
// &info_hash=%ce%cf%dc%0c%07%5d%22x%02%a79%0a-%ea%14%cb%a3s%af%eb
// &peer_id=-qB4380-mB)0B.0nhN6V
// &port=8110
// &uploaded=0
// &downloaded=0
// &left=0
// &corrupt=0
// &key=06825FE3
// &event=started
// &numwant=200
// &compact=1
// &no_peer_id=1
// &supportcrypto=1
// &redundant=0
// &ipv6=2409%3a8a28%3ac6c%3ad80%3a3881%3ae173%3a5a18%3a3a79
// &ipv6=2409%3a8a28%3ac6c%3ad80%3a540d%3a17db%3a840f%3a5f9f

 * @author ：xiezc
 * @date   ：2022/4/29 2:29 PM
 */
@Entity
@Table(schema = "vxpt")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class Peer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?,
    val authKey: String,
    val userId: Int,
    val torrentId: Int,
    val infoHash: String,
    val peerId: String,
    var trackerId: String?,

    @Embedded
    var ipAddr: IpAddr,

    var downloaded: Long,
    var uploaded: Long,
    var left: Long,

    var event: String,
    var numwant: Int,

    var status: Int,
    val createTime: LocalDateTime,
    var updateTime: LocalDateTime,
) {

    fun save(peerRepository: PeerRepository): Peer {
        return peerRepository.save(this)
    }

    /**
     * 校验,  落库,  返回流水对象
     */
    fun checkAndLogEvent(){

    }

    fun findPeers(peerRepository: PeerRepository): List<Peer> {
        return peerRepository.findAllByInfoHashAndUserIdNotInAndStatus(this.infoHash, setOf(this.userId), 0)
    }

}

@Embeddable
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
data class IpAddr(
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var ip: List<String>?,
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var ipv6: List<String>?,
    var port: Int,
    var compact: Int,
)