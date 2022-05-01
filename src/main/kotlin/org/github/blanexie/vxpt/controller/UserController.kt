package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.account.model.User
import org.github.blanexie.vxpt.account.service.UserService
import org.github.blanexie.vxpt.support.WebResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/user")
class UserController(val userService: UserService) {

    /**
     * 注册用户
     */
    @PostMapping("add")
    fun add(@RequestBody user: User): Mono<WebResponse> {
        return Mono.fromCallable {
            val save = user.save(userRepository = userService.userRepository)
            WebResponse(data = save)
        }
    }

    @PostMapping("loginByNickName")
    fun loginByNickName(@RequestBody user: User): Mono<WebResponse> {
        return Mono.fromCallable {
            val user = userService.loginByNickName(user.nickName, user.pwd)
            WebResponse(data = user)
        }
    }

    @PostMapping("loginByEmail")
    fun loginByEmail(@RequestBody user: User): Mono<WebResponse> {
        return Mono.fromCallable {
            val user = userService.loginByEmail(user.email, user.pwd)
            WebResponse(data = user)
        }
    }

}