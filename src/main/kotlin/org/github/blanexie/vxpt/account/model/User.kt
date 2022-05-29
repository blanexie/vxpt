package org.github.blanexie.vxpt.account.model

import cn.hutool.core.util.IdUtil
import cn.hutool.crypto.digest.DigestUtil
import com.fasterxml.jackson.annotation.JsonFormat
import org.github.blanexie.vxpt.account.dao.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH")

@Entity
@Table(schema = "vxpt")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(unique = true)
    var nickName: String,
    @Column(unique = true)
    var email: String,
    var pwd: String?,
    var sex: Int, //0:未知, 1:男   2:女

    //当前用户的介绍人
    var referencesUserId: Int,

    var status: Int,
    //登录的token
    @Column(unique = true)
    var token: String? = null,
    //token的有效期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var tokenExpireTime: LocalDateTime? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createTime: LocalDateTime = LocalDateTime.now(),
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {

    fun save(userRepository: UserRepository): User {
        return userRepository.save(this)
    }

    fun checkPwd(pwd: String): Boolean {
        val format = LocalDateTime.now().format(dateTimeFormatter)
        return DigestUtil.md5Hex(this.pwd + format) == pwd
    }

    fun createToken() {
        this.token = IdUtil.fastSimpleUUID()
        this.tokenExpireTime = LocalDateTime.now().plusDays(14)
    }

    fun deleteToken() {
        this.token = null
        this.tokenExpireTime = null
    }


    fun checkToken(): Boolean {
        return this.tokenExpireTime!!.isAfter(LocalDateTime.now())
    }

}