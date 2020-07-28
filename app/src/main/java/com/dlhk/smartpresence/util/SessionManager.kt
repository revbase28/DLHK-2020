package com.dlhk.smartpresence.util

import android.content.Context
import android.content.SharedPreferences
import com.dlhk.smartpresence.util.Constant.Companion.SESSION
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_NAME
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_REGION
import com.dlhk.smartpresence.util.Constant.Companion.SESSION_ROLE
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

    fun getSessionName() : String? { return preferences.getString(SESSION_NAME, "")}
    fun getSessionRole() : String? { return preferences.getString(SESSION_ROLE, "")}
    fun getSessionZone() : String? {return preferences.getString(SESSION_ZONE, "-")}
    fun getSessionRegion() : String? { return preferences.getString(SESSION_REGION, "-")}

}