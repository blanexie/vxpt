package org.github.blanexie.vxpt.dht

import java.time.LocalDateTime


/**
 * 节点对象
 */
class Node(
    // 节点id
   val id: String,
   var token: String?,
   var q: String?, //当前节点的请求状态, 用来标识本应用对这个点发出了怎样的请求, 并且在等待回应中;  init: 标识空闲
   var updateTime: Long,  //最新一次更新的时间戳
)