package com.github.blanexie.vxpt.auth.server.domain

import cn.hutool.core.bean.BeanUtil
import com.github.blanexie.vxpt.auth.api.dto.PermissionDTO
import com.github.blanexie.vxpt.auth.api.dto.RoleDTO
import com.github.blanexie.vxpt.auth.server.domain.factory.RoleFactory


/**
 * 角色权限
 */
class RoleService(
    private val roleFactory: RoleFactory
) {

    fun findByRoleCode(code: String): RoleDTO? {
        val roleEntity = roleFactory.findByCode(code)
        val let = roleEntity?.let {
            val roleDTO = BeanUtil.copyProperties(
                roleEntity,
                RoleDTO::class.java,
                "permissions"
            )
            roleDTO.permissions = roleEntity.permissions.map {
                BeanUtil.copyProperties(it, PermissionDTO::class.java)
            }.toMutableSet()
            for (role in it.roles) {
                val subRoleDTO = this.findByRoleCode(code)
                subRoleDTO?.let {
                    roleDTO.permissions.addAll(it.permissions)
                }
            }
            roleDTO
        }



        return let
    }


}