package org.github.blanexie.vxpt.account.service

import org.github.blanexie.vxpt.account.dao.UserRepository
import org.github.blanexie.vxpt.account.model.User
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun loginByNickName(nickName: String, pwd: String): User? {
        val user = userRepository.findFirstByNickNameAndStatus(nickName, 0)
        val checkPwd = user.checkPwd(pwd)
        if (checkPwd) {
            return user
        }
        return null
    }

    fun loginByEmail(email: String, pwd: String): User? {
        val user = userRepository.findFirstByEmailAndStatus(email, 0)
        val checkPwd = user.checkPwd(pwd)
        if (checkPwd) {
            return user
        }
        return null
    }


}