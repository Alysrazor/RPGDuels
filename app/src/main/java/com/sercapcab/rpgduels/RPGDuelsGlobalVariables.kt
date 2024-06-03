package com.sercapcab.rpgduels

import android.util.Log
import com.sercapcab.rpgduels.game.entity.Account
import com.sercapcab.rpgduels.game.entity.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.util.UUID

var account: Account? = null
var selectedCharacter: Character? = null
