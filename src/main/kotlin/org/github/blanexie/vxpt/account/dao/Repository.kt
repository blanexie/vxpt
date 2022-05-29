package org.github.blanexie.vxpt.account.dao

import org.github.blanexie.vxpt.account.model.Invitation
import org.github.blanexie.vxpt.account.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Int> {

    fun findByNickNameAndStatus(nickName: String, status: Int): User?

    fun findByEmailAndStatus(email: String, status: Int): User?

    fun findByTokenAndStatus(token: String, status: Int): User?

}

@Repository
interface InvitationRepository : CrudRepository<Invitation, Int> {

    fun findByCode(code: String): Invitation?
}