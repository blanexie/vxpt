package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.account.dto.UserDTO
import org.github.blanexie.vxpt.account.service.UserService
import org.github.blanexie.vxpt.support.WebResponse
import org.github.blanexie.vxpt.support.filter.NotLogin
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

    /**
     * 注册用户
     */
    @NotLogin
    @PostMapping("register")
    fun add(@RequestBody userDTO: UserDTO): Mono<WebResponse> {
        return Mono.fromCallable {
            WebResponse(data = userService.register(userDTO))
        }
    }

    @NotLogin
    @PostMapping("login")
    fun login(@RequestBody userDTO: UserDTO): Mono<WebResponse> {
        return Mono.fromCallable {
            val user = userService.login(userDTO)
            user?.let {
                WebResponse(data = user.token)
            } ?: WebResponse(403, "登录失败")
        }
    }

    @GetMapping("logout")
    fun logout(@RequestHeader token: String): Mono<WebResponse> {
        return Mono.fromCallable {
            userService.logout(token)
            WebResponse()
        }
    }

}