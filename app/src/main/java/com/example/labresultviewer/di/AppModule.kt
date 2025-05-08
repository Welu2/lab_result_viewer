// di/AppModule.kt
package com.example.labresultviewer.di

import android.content.Context
import com.example.labresultviewer.data.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionManager(context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    fun provideContext(application: android.app.Application): Context {
        return application.applicationContext
    }

}
