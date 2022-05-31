package org.github.blanexie.vxpt.common.service

import org.github.blanexie.vxpt.common.dao.PropRepository
import org.github.blanexie.vxpt.common.model.Prop
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PropService {

    @Autowired
    lateinit var propRepository: PropRepository

    fun getByKey(key: String): Prop {
        val prop = propRepository.findByKey(key)
        return prop
    }

}