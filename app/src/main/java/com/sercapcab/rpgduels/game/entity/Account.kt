package com.sercapcab.rpgduels.game.entity

import com.sercapcab.rpgduels.Since
import com.sercapcab.rpgduels.api.serializer.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Since(version = "1.0")
class Account(
    @Serializable(with = UUIDSerializer::class)
    val accountUuid: UUID,
    var username: String,
    var email: String,
    var accountLocked: Boolean,
    var characters: List<Character> = emptyList(),
    var activeCharacter: Character?
)