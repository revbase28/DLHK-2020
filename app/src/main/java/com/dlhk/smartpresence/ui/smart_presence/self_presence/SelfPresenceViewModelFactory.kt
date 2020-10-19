package com.dlhk.smartpresence.ui.smart_presence.self_presence

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AttendanceRepo

class SelfPresenceViewModelFactory(
    val attendanceRepo: AttendanceRepo,
    val app: Application
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SelfPresenceViewModel(
            attendanceRepo,
            app
        ) as T
    }
}