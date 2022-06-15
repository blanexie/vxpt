package org.github.blanexie.vxpt.support.filter

import cn.hutool.cache.CacheUtil
import cn.hutool.core.annotation.AnnotationUtil
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.github.blanexie.vxpt.account.model.User
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Aspect
class AuthAspect {


    val cache = CacheUtil.newLRUCache<String, Boolean>(100, 3600000)

    @Pointcut("execution(public * org.github.blanexie.vxpt.controller.*.*(..))")
    fun classRule() {
    }

    //带有AuthIgnore注解的不校验
    @Pointcut("@annotation(NotLogin)")
    fun ignoreRule() {
    }

    @Around("classRule() && ignoreRule()")
    fun aroundInvoke(joinPoint: ProceedingJoinPoint): Mono<Object> {
        val declaringType = joinPoint.signature.declaringType
        val declaringTypeName = joinPoint.signature.declaringTypeName
        val name = joinPoint.signature.name
        val key = declaringTypeName + name
        val notLogin = cache.get(key) {
            val method = declaringType.declaredMethods.first { it.name == name }
            val annotation = method.getAnnotation(NotLogin::class.java)
            annotation != null
        }
        if (notLogin) {
            return joinPoint.proceed() as Mono<Object>
        } else {
            val exchange = Mono.deferContextual {
                Mono.just(it.get("exchange") as ServerWebExchange)
            }
            val flatMap = exchange.map { it.session.map { it.getAttribute<User>("user") } }
                .flatMap {
                    it?.let {
                        joinPoint.proceed() as Mono<Object>
                    }
                }
            return flatMap
        }
    }


}