package org.github.blanexie.vxpt.support.filter

import org.github.blanexie.vxpt.account.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class AuthWebFilter : WebFilter {

    @Autowired
    lateinit var userService: UserService

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val token = request.headers.getFirst("token")
        val user = token?.let {
            val user = userService.findByToken(token)
            if (user?.checkToken() == true) {
                exchange.session.map { ws ->
                    ws.attributes["user"] = user
                }.subscribe()
            }
            user
        }
        return chain.filter(exchange).contextWrite {
            it.put("exchange", exchange)
        }

    }
}
