package org.github.blanexie.vxpt.support

data class WebResponse(
    val code: Int = 200,
    val msg: String = "",
    val time: Long = System.currentTimeMillis(),
    val data: Any? = null
)