package com.github.blanexie.vxpt.user.controller

import com.github.blanexie.vxpt.user.api.UserRpc
import com.github.blanexie.vxpt.user.api.dto.RegisterUserDTO
import com.github.blanexie.vxpt.user.api.dto.RoleDTO
import com.github.blanexie.vxpt.user.api.dto.UserDTO
import com.github.blanexie.vxpt.user.service.InvitationService
import com.github.blanexie.vxpt.user.service.RoleService
import com.github.blanexie.vxpt.user.service.UserService
import com.github.blanexie.vxpt.user.util.TokenComponent
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
class UserRpcController(
    @Resource
    val userService: UserService,
    @Resource
    val roleService: RoleService,
    @Resource
    val invitationService: InvitationService,
    @Resource
    val tokenComponent: TokenComponent
) : UserRpc {


    override fun userInfo(token: String): UserDTO? {
        val decrypt = tokenComponent.getAES().decrypt(token)
        val split = String(decrypt).split("|")
        val useId = split[0].toInt()
        val createTime = split[1].toLong()

        if (createTime + tokenComponent.getExpireSecond() * 1000 < System.currentTimeMillis()) {
            return null
        } else {
            return getUserDTO(useId)
        }
    }

    private fun getUserDTO(useId: Int): UserDTO {
        val userDO = userService.findById(useId)
        val roles = userDO!!.roles.mapNotNull { roleService.findRole(it) }
            .map {
                RoleDTO(it.id, it.name, it.code, it.permissionCodes)
            }
        return UserDTO(useId, userDO.nickName, userDO.email, userDO.sex, roles)
    }


    override fun login(email: String, pwdSecret: String): String? {
        val userDO = userService.login(email, pwdSecret) ?: return null
        return tokenComponent.buildToken(userDO.id)
    }

    override fun register(registerUserDTO: RegisterUserDTO): UserDTO {
        val invitationDO = invitationService.findByCode(registerUserDTO.code)?: throw  Error("请输入正确的邀请码")
        val nextUserId = userService.nextUserId()
        //检查邀请函
        invitationDO.use(nextUserId)

        val userId = userService.register(
            registerUserDTO
        )
        return getUserDTO(userId)
    }

}