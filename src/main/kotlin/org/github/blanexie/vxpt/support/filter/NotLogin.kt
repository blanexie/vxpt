package org.github.blanexie.vxpt.support.filter


@Retention
@Target(
    AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
annotation class NotLogin()
