package com.github.blanexie.vxpt.user.rpc

import com.github.blanexie.vxpt.user.api.UserRpc
import com.github.blanexie.vxpt.user.api.dto.UserDTO
import com.github.blanexie.vxpt.user.domain.application.UserAndAccountService
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource


@RestController
class UserRpcImpl : UserRpc {

    @Resource
    lateinit var userAndAccountService: UserAndAccountService

    override fun login(email: String, pwdSecret: String, timeStamp: Long): UserDTO? {
        return userAndAccountService.login(email, pwdSecret, timeStamp)
    }


}