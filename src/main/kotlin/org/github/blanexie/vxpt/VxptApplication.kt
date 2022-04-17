package org.github.blanexie.vxpt

import org.github.blanexie.vxpt.post.dao.ArticleRepository
import org.github.blanexie.vxpt.post.dao.UserRepository
import org.github.blanexie.vxpt.post.model.Article
import org.github.blanexie.vxpt.post.model.User
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class VxptApplication{

	@Bean
	fun databaseInitializer(userRepository: UserRepository,
							articleRepository: ArticleRepository
	) = ApplicationRunner {

		val smaldini = userRepository.save(User("smaldini", "Stéphane", "Maldini"))
		articleRepository.save(
			Article(
			title = "Reactor Bismuth is out",
			headline = "Lorem ipsum",
			content = "dolor sit amet",
			author = smaldini
		)
		)
		articleRepository.save(Article(
			title = "Reactor Aluminium has landed",
			headline = "Lorem ipsum",
			content = "dolor sit amet",
			author = smaldini
		))
	}
}

fun main(args: Array<String>) {
	runApplication<VxptApplication>(*args)
}
