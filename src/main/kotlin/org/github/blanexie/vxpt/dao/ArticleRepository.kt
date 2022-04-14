package org.github.blanexie.vxpt.dao

import org.github.blanexie.vxpt.model.Article
import org.github.blanexie.vxpt.model.User
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {
  fun findBySlug(slug: String): Article?
  fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}

interface UserRepository : CrudRepository<User, Long> {
  fun findByLogin(login: String): User?
}