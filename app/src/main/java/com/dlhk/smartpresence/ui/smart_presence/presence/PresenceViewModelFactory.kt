package com.dlhk.smartpresence.ui.smart_presence.presence

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.repositories.AttendanceRepo

class PresenceViewModelFactory(
    val repo: AttendanceRepo,
    val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PresenceViewModel(repo, app) as T
    }
}