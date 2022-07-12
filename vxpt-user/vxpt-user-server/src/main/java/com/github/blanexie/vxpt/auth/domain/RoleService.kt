package com.github.blanexie.vxpt.auth.domain

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import com.github.blanexie.vxpt.auth.api.dto.PermissionDTO
import com.github.blanexie.vxpt.auth.api.dto.RoleDTO
import com.github.blanexie.vxpt.auth.domain.factory.PathFactory
import com.github.blanexie.vxpt.auth.domain.factory.RoleFactory


/**
 * 角色权限
 */
class RoleService(
    private val roleFactory: RoleFactory,
    private val pathFactory: PathFactory
) {

    fun findByRoleCode(code: String): RoleDTO? {
        val roleEntity = roleFactory.findByCode(code)
        val let = roleEntity?.let {
            val roleDTO = BeanUtil.copyProperties(
                roleEntity,
                RoleDTO::class.java,
                "permissions"
            )
            val permissions = roleEntity.permissions.map {
                BeanUtil.copyProperties(it, PermissionDTO::class.java)
            }.toMutableSet()
            roleDTO.permissions = permissions

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

    /**
     * 校验权限
     */
    fun checkPath(path: String, permissionCode: Set<String>): Boolean {
        val pathEntity = pathFactory.findByPath(path)
        return pathEntity?.let {
            val intersection = CollUtil.intersection(it.permissions, permissionCode)
            intersection.isEmpty()
        } ?: false
    }


}