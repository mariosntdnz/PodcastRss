package com.example.podcastrss.application

import android.app.Application
import com.example.podcastrss.di.datasource_module.dataSourceModule
import com.example.podcastrss.di.repository_module.repositoryModule
import com.example.podcastrss.di.service.serviceModule
import com.example.podcastrss.di.usecase_module.useCaseModule
import com.example.podcastrss.di.viewmodels_module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(viewModelModule)
            modules(repositoryModule)
            modules(dataSourceModule)
            modules(serviceModule)
            modules(useCaseModule)
        }
    }
}