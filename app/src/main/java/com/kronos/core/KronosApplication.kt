package com.kronos.core

import android.app.Application
import com.kronos.core.data.local.KronosDatabase
import com.kronos.core.data.repository.KronosRepository
import com.kronos.core.domain.repository.IKronosRepository

class KronosApplication : Application() {

    lateinit var repository: IKronosRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = KronosDatabase.getDatabase(this)
        repository = KronosRepository(
            serverDao = database.serverDao(),
            alertDao = database.alertDao()
        )
    }
}
