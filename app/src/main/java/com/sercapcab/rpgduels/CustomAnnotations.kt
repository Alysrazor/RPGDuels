package com.sercapcab.rpgduels

import kotlin.annotation.AnnotationRetention
import kotlin.annotation.AnnotationTarget

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Since(val version: String)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Works
