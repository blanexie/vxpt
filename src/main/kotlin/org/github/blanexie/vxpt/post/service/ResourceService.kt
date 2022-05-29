package org.github.blanexie.vxpt.post.service

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.crypto.digest.DigestUtil
import cn.hutool.crypto.digest.MD5
import org.github.blanexie.vxpt.post.dao.ResourceRepository
import org.github.blanexie.vxpt.post.dao.TorrentRepository
import org.github.blanexie.vxpt.post.model.Resource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono
import java.nio.file.Path


@Service
class ResourceService(val resourceRepository: ResourceRepository) {


    /**
     * 上传文件
     * type: 文件类型
     */
    fun upload(file: FilePart, type: String, userId: Int): Mono<Resource> {
        val originalFilename = file.filename()
        val suffix = originalFilename?.substringAfterLast(".")
        return file.content().map {
            val byteArray = it.asByteBuffer().array()
            val md5Hex = DigestUtil.md5Hex(byteArray)
            val path = "$type/$md5Hex.$suffix"
            var resource = resourceRepository.findByMd5(md5Hex)
            if (resource == null) {
                FileUtil.writeBytes(byteArray, Path.of(path).toFile())
                resource = Resource(null, type, path, md5Hex, originalFilename, byteArray.size.toLong(), suffix, userId)
                resourceRepository.save(resource)
            }
            resource
        }.last()
    }

}