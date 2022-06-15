package org.github.blanexie.vxpt.controller

import org.github.blanexie.vxpt.account.model.User
import org.github.blanexie.vxpt.post.service.ResourceService
import org.github.blanexie.vxpt.support.WebResponse
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/api/resource")
class ResourceController(val resourceService: ResourceService) {

    @PostMapping("upload")
    fun upload(
        @SessionAttribute("user") user: User,
        @RequestPart("type") type: String,
        @RequestPart("file") file: FilePart
    ): Mono<WebResponse> {
        return resourceService.upload(file, type, user.id!!)
            .map { WebResponse(data = it) }
    }

    @GetMapping("labels")
    fun labels(): Mono<WebResponse> {
        return resourceService.labels()
            .map { WebResponse(data = it) }
    }


}