package org.github.blanexie.vxpt.dht

import cn.hutool.cache.CacheUtil
import cn.hutool.cache.impl.LRUCache
import cn.hutool.core.collection.BoundedPriorityQueue
import java.util.*
import kotlin.collections.HashMap

/**
 * 路由表
 */
class Router(
    val id: String,
    val nodes: HashMap<String,Node>
)