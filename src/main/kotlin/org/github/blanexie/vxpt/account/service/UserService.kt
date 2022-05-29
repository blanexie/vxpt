package org.github.blanexie.vxpt.account.service

import org.github.blanexie.vxpt.account.dao.InvitationRepository
import org.github.blanexie.vxpt.account.dao.UserRepository
import org.github.blanexie.vxpt.account.dto.UserDTO
import org.github.blanexie.vxpt.account.model.User
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val invitationRepository: InvitationRepository
) {

    fun findByToken(token: String): User? {
        return userRepository.findByTokenAndStatus(token, 0)
    }

    /**
     *  注册
     */
    fun register(userDTO: UserDTO): Boolean {
        //获取邀请函
        val invitation = invitationRepository.findByCode(userDTO.code!!)

        return if (invitation == null) {
            false
        } else if (invitation.check(userDTO.email!!)) {
            //通过校验
            val user = User(
                null, userDTO.nickName!!,
                userDTO.email!!,
                userDTO.pwd!!,
                userDTO.sex!!,
                invitation.userId!!,
                0
            )
            user.save(userRepository)
            invitation.used(invitationRepository)    //标记邀请函被使用
            true
        } else false

    }

    fun login(userDTO: UserDTO): User? {
        var user =
            if (userDTO.email != null) {
                userRepository.findByEmailAndStatus(userDTO.email, 0)
            } else if (userDTO.nickName != null) {
                userRepository.findByNickNameAndStatus(userDTO.nickName, 0)
            } else {
                null
            }
        return user?.let {
            if (it.checkPwd(userDTO.pwd!!)) {
                it.createToken() //生成秘钥
                user.save(userRepository)
                it
            } else null
        }
    }

    fun logout(token: String) {
        val user = userRepository.findByTokenAndStatus(token, 0)
        user?.deleteToken()
        user?.save(userRepository)
    }

}