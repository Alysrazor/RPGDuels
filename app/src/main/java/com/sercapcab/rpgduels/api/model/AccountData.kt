package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import com.sercapcab.rpgduels.game.entity.Account
import com.sercapcab.rpgduels.game.entity.Character
import java.util.UUID

data class AccountData(
    @SerializedName("accountUuid") val accountUuid: UUID,
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("accountLocked") var accountLocked: Boolean,
    @SerializedName("characters") var characters: List<CharacterData>?
) {
    fun toAccount(): Account {
        return Account(
            accountUuid = accountUuid,
            username = username,
            email = email,
            accountLocked = accountLocked,
            characters = characters?.map { it.toCharacter() } ?: emptyList(),
            activeCharacter = null
        )
    }
}
