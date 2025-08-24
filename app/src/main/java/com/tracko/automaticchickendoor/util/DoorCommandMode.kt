package com.tracko.automaticchickendoor.util

enum class DoorCommandMode(val commandPrefix: String) {
    MODE_TT("Mode_TT"), // Time-Time
    MODE_TL("Mode_TL"), // Time-Light
    MODE_LT("Mode_LT"), // Light-Time
    MODE_LL("Mode_LL"); // Light-Light
    
    fun formatCommand(param1: String, param2: String): String {
        return "$commandPrefix;$param1;$param2"
    }
}
