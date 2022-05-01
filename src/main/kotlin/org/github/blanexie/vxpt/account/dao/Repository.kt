package org.github.blanexie.vxpt.account.dao

import org.github.blanexie.vxpt.account.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {

    fun findFirstByNickNameAndStatus(nickName: String, status: Int): User

    fun findFirstByEmailAndStatus(email: String, status: Int): User
}