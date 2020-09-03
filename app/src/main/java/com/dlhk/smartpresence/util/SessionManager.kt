package com.dlhk.smartpresence.util

import android.content.Context
import android.content.SharedPreferences
import com.dlhk.smartpresence.util.Constant.Companion.SESSION
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_BOOLEAN
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_ID
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_LOGIN_DATE
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_NAME
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_PHOTO
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_REGION
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_ROLE
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_SHIFT
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_ZONE

class SessionManager(val context: Context) {

    var preferences: SharedPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
    lateinit var editor: SharedPreferences.Editor

    init {
        editor = preferences.edit()
    }

    fun saveSessionName(name: String){
        editor.apply {
            putString(SESSION_NAME, name)
            commit()
        }
    }

    fun saveSessionRole(role: String){
        editor.apply {
            putString(SESSION_ROLE, role)
            commit()
        }
    }

    fun saveSessionZone(zone :String){
        editor.apply{
            putString(SESSION_ZONE, zone)
            commit()
        }
    }

    fun saveSessionRegion(region: String){
        editor.apply {
            putString(SESSION_REGION, region)
            commit()
        }
    }

    fun saveSessionUserId(id: String){
        editor.apply {
            putString(SESSION_ID, id)
            commit()
        }
    }

    fun saveSessionPhotoString(photo: String){
        editor.apply {
            putString(SESSION_PHOTO, photo)
            commit()
        }
    }

    fun saveSessionShift(shift: String){
        editor.apply {
            putString(SESSION_SHIFT, shift)
            commit()
        }
    }

    fun saveSessionBoolean(isLogin: Boolean){
        editor.apply {
            putBoolean(SESSION_BOOLEAN, isLogin)
            commit()
        }
    }

    fun saveSessionLoginDate(date: String){
        editor.apply {
            putString(SESSION_LOGIN_DATE, date)
            commit()
        }
    }

    fun getSessionName() : String? { return preferences.getString(SESSION_NAME, "")}
    fun getSessionRole() : String? { return preferences.getString(SESSION_ROLE, "")}
    fun getSessionZone() : String? {return preferences.getString(SESSION_ZONE, "-")}
    fun getSessionRegion() : String? { return preferences.getString(SESSION_REGION, "-")}
    fun getSessionId() : String? { return preferences.getString(SESSION_ID, "-")}
    fun getSessionPhoto(): String { return preferences.getString(SESSION_PHOTO, "").toString()}
    fun getSessionShift(): String { return preferences.getString(SESSION_SHIFT, "").toString()}
    fun getSessionIsLogin(): Boolean { return preferences.getBoolean(SESSION_BOOLEAN, false)}
    fun getSessionLoginDate(): String { return preferences.getString(SESSION_LOGIN_DATE, "").toString()}

}