package org.github.blanexie.vxpt.support

import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.HexUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.digest.DigestUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 *
 * @author ：xiezc
 * @date   ：2022/4/29 3:26 PM
 */
@Component
class AuthUtil(
    @Value("#{vxpt.auth.secret}")
    val secret: String
) {

    private val aes = SecureUtil.aes(DigestUtil.sha256(secret))

    fun hexEncode(data: ByteArray): String {
        return HexUtil.encodeHexStr(data)
    }

    fun hexDecode(data: String): ByteArray {
        return HexUtil.decodeHex(data)
    }

    fun encrypt(data: ByteArray): ByteArray {
        val encrypt = aes.encrypt(data)
        return encrypt
    }

    fun encryptHex(data: String): String {
        val encrypt = aes.encrypt(data, CharsetUtil.CHARSET_UTF_8)
        return HexUtil.encodeHexStr(encrypt)
    }

    fun decrypt(data: ByteArray): ByteArray {
        val decrypt = aes.decrypt(data)
        return decrypt
    }

}