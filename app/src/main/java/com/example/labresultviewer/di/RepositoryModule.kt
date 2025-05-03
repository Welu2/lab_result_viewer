package com.example.labresultviewer.di

import com.example.labresultviewer.network.LabResultService
import com.example.labresultviewer.repository.LabResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLabResultRepository(service: LabResultService): LabResultRepository {
        return LabResultRepository(service)
    }
}