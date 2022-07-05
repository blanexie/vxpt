package com.github.blanexie.vxpt.auth.server.domain.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 权限表
 */
@Data
@Entity
@Table(schema = "vxpt-auth")
public class AuthorityDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 权限的code
     */
    @Column(unique = true)
    private String code;


    /**
     * 权限的说明
     */
    private String description;





}
