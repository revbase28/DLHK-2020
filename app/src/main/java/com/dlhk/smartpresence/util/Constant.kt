package com.dlhk.smartpresence.util

import com.dlhk.smartpresence.SmartPresenceApp

class Constant {
    companion object{
        const val BASE_URL = "https://7c628dbb6c72.ngrok.io/api/"
        const val REQUEST_IMAGE_CAPTURE = 1
        const val SESSION = "session"
        const val SESSION_NAME = "name"
        const val SESSION_ROLE = "role"
        const val SESSION_ZONE = "zone"
        const val SESSION_REGION = "region"
        const val SESSION_ID = "user_id"
        const val SESSION_PHOTO = "photo"
        const val SESSION_SHIFT = "shift"
        val ACTION_DIALOG_REASON = arrayOf("Hilang", "Rusak")
        const val DIALOG_REASON_HILANG = 0
        const val DIALOG_REASON_RUSAK = 1
        const val LOCATION_REQUEST = 3
        const val DRAINAGE = "drainage"
        const val SWEEPER = "sweeper"
        const val GARBAGE_COLLECTOR = "garbage"
        val ACTION_PERMIT_REASON = arrayOf("Izin", "Sakit", "Alfa")
        const val DIALOG_PERMIT_ALFA = 2
        const val DIALOG_PERMIT_SICK = 1
        const val DIALOG_PERMIT_LEAVE = 0
        const val PERMIT = "Terkonfirmasi"
        const val ALFA = "Alfa"
    }
}