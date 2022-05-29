package org.github.blanexie.vxpt.support

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.stereotype.Component
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *
 * @author ：xiezc
 * @date   ：2022/5/5 5:38 PM
 */
@Component
class ApplicationEventMulticasterConfig {

    @Bean
    fun simpleApplicationEventMulticaster(beanFactory: BeanFactory): SimpleApplicationEventMulticaster {
        val simpleApplicationEventMulticaster = SimpleApplicationEventMulticaster(beanFactory)
        val threadPoolExecutor = ThreadPoolExecutor(
            5,
            50,
            30000L,
            TimeUnit.SECONDS,
            ArrayBlockingQueue(2000)
        )
        simpleApplicationEventMulticaster.setTaskExecutor(threadPoolExecutor)
        return simpleApplicationEventMulticaster
    }

}