package com.sercapcab.rpgduels.game.entity.exception

class NotEnoughPowerException: Exception {
    constructor(): super()

    constructor(msg: String): super(msg)
}