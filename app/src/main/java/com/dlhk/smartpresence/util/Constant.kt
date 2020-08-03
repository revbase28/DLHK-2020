package com.dlhk.smartpresence.util

import com.dlhk.smartpresence.SmartPresenceApp

class Constant {
    companion object{
        const val BASE_URL = "https://7e62b6014220.ngrok.io/api/"
        const val REQUEST_IMAGE_CAPTURE = 1
        const val SESSION = "session"
        const val SESSION_NAME = "name"
        const val SESSION_ROLE = "role"
        const val SESSION_ZONE = "zone"
        const val SESSION_REGION = "region"
        val ACTION_DIALOG_REASON = arrayOf("Hilang", "Rusak")
        const val DIALOG_REASON_HILANG = 0
        const val DIALOG_REASON_RUSAK = 1
        const val LOCATION_REQUEST = 3
        const val DRAINAGE = "drainage"
        const val SWEEPER = "sweeper"
        const val GARBAGE_COLLECTOR = "garbage"
    }
}