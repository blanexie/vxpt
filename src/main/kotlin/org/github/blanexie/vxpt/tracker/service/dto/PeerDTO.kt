package org.github.blanexie.vxpt.tracker.service.dto

import com.fasterxml.jackson.annotation.JsonAlias

/**
 *   ?auth_key=e934b04ba3234d639c0523f0cb15fe0q
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
 * @date   ：2022/4/29 2:54 PM
 */
class PeerDTO(
    @JsonAlias("auth_key")
    val authKey: String,
    @JsonAlias("info_info")
    val infoHash: String,
    @JsonAlias("peer_id")
    val peerId: String,
    @JsonAlias("no_peer_id")
    val noPeerId: Int,

    val uploaded: Long,
    val downloaded: Long,
    val left: Long,
    val event: String,
    val numwant: Int,

    val corrupt: Int,
    val key: String,
    val redundant: Int,

    @JsonAlias("supportcrypto")
    val supportCrypto: Int,

    val ipv6: List<String>?,
    val ip: List<String>?,
    val port: Int,
    val compact: Int,
) {


}

