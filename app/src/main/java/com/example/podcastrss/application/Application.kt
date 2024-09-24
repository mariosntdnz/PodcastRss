package com.example.podcastrss.application

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.example.podcastrss.di.datasource_module.dataSourceModule
import com.example.podcastrss.di.repository_module.repositoryModule
import com.example.podcastrss.di.service.serviceModule
import com.example.podcastrss.di.store.storeModule
import com.example.podcastrss.di.usecase_module.useCaseModule
import com.example.podcastrss.di.viewmodels_module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(viewModelModule)
            modules(repositoryModule)
            modules(dataSourceModule)
            modules(serviceModule)
            modules(useCaseModule)
            modules(storeModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.10)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}