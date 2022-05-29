package org.github.blanexie.vxpt.support.filter

import org.github.blanexie.vxpt.account.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthWebFilter : WebFilter {


    @Autowired
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    @Autowired
    lateinit var userService: UserService


    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        return requestMappingHandlerMapping.getHandler(exchange)
            .switchIfEmpty(chain.filter(exchange))
            .flatMap {
                (it as HandlerMethod).let { handlerMethod ->
                    val token = exchange.request.headers.getFirst("token")
                    val user = token?.let {
                        val user = userService.findByToken(token)
                        if (user?.checkToken() == true) {
                            exchange.session.map { ws ->
                                ws.attributes["user"] = user
                            }.subscribe()
                        }
                        user
                    }

                    val notLogin = (handlerMethod.method.getAnnotation(NotLogin::class.java)
                        ?: it.beanType.getAnnotation(NotLogin::class.java))

                    if (notLogin != null) {
                        //不需要登录, 直接通过
                        chain.filter(exchange)
                    } else {
                        //需要登录, 检查有无用户信息
                        if (user == null) {
                            //没有用户信息, 直接返回错误码
                            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                            Mono.empty()
                        } else {
                            //继续
                            chain.filter(exchange)
                        }
                    }
                }
            }
    }
}
