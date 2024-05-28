package com.sercapcab.rpgduels.exception

public class DeprecatedException(msg: String = "Hay una parte del código o una idea que está obsoleta. Elimínala"): RuntimeException(msg)

fun DEPRECATED(reason: String): Nothing = throw DeprecatedException("Hay una parte del código o una idea que está obsoleta. Elimínala\nReason: $reason")