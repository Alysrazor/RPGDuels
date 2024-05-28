package com.sercapcab.rpgduels.api.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Account(
    @SerializedName("accountUuid") val accountUuid: UUID,
    @SerializedName("username") var username: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("accountLocked") var accountLocked: Boolean,
    @SerializedName("characters") var characters: Set<Character>
)
