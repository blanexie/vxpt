package org.github.blanexie.vxpt.account.model

import org.github.blanexie.vxpt.account.dao.UserRepository
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(schema = "vxpt")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    @Column(unique = true)
    var nickName: String,
    @Column(unique = true)
    var email: String,
    var pwd: String,
    var sex: Int, //0:未知, 1:男   2:女

    var status: Int,
    var createTime: LocalDateTime = LocalDateTime.now(),
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {

    fun save(userRepository: UserRepository): User {
        return userRepository.save(this)
    }

    fun checkPwd(pwd: String): Boolean {
        return this.pwd == pwd
    }

}