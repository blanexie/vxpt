package org.github.blanexie.vxpt.account.model

import org.github.blanexie.vxpt.account.dao.InvitationRepository
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 邀请函, 邀请记录表
 */
@Entity
@Table(schema = "vxpt")
class Invitation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(unique = true)
    var code: String,    //邀请码

    @Column(unique = true)
    var email: String,   //受邀者邮箱

    var userId: Int,   //发出邀请函的用户id

    var expireTime: LocalDateTime,  //邀请函过期时间

    var status: Int,
    var createTime: LocalDateTime = LocalDateTime.now(),
    var updateTime: LocalDateTime = LocalDateTime.now(),
) {


    /**
     * 校验邀请是否过期, 并校验传入的邮箱是否符合邀请
     */
    fun check(email: String): Boolean {
        if (this.email.equals("*")) {
            return true
        }
        if (status == 1) {
            //已经使用过了
            return false
        }
        if (LocalDateTime.now().isAfter(this.expireTime)) {
            return false
        }
        if (email.equals(this.email)) {
            return true
        }
        return false
    }

    //标记邀请函 已经使用了
    fun used(invitationRepository: InvitationRepository) {
        if (this.email.equals("*")) {
            return
        }
        this.status = 1
        invitationRepository.save(this)
    }


}