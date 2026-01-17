package com.example.freshcycle

import android.app.Application
import com.example.freshcycle.repositori.ContainerApp
import com.example.freshcycle.repositori.DefaultContainerApp

class FreshCycleApplication : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainerApp()
    }
}