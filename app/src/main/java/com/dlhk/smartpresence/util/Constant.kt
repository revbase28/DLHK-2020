package com.dlhk.smartpresence.util

import com.dlhk.smartpresence.SmartPresenceApp

class Constant {
    companion object{
        const val BASE_URL = "https://5345231d71d0.ngrok.io/api/"
        const val REQUEST_IMAGE_CAPTURE = 1

        const val SESSION = "session"
        const val SESSION_NAME = "name"
        const val SESSION_ROLE = "role"
        const val SESSION_ZONE = "zone"
        const val SESSION_REGION = "region"
        const val SESSION_ID = "user_id"
        const val SESSION_PHOTO = "photo"
        const val SESSION_SHIFT = "shift"
        const val SESSION_BOOLEAN = "isLogin"
        const val SESSION_LOGIN_DATE = "loginDate"

        val ACTION_DIALOG_REASON = arrayOf("Hilang", "Rusak")
        const val DIALOG_REASON_HILANG = 0
        const val DIALOG_REASON_RUSAK = 1

        const val LOCATION_REQUEST = 3
        const val LOCATION_UPDATE_INTERVAL = 10000L
        const val FASTEST_LOCATION_INTERVAL = 8000L

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