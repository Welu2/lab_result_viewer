package com.example.labresultviewer.di

import com.example.labresultviewer.network.AdminAppointmentService
import com.example.labresultviewer.network.AdminLabResultService
import com.example.labresultviewer.network.ApiService
import com.example.labresultviewer.network.LabResultService
import com.example.labresultviewer.repository.AdminAppointmentsRepository
import com.example.labresultviewer.repository.AdminLabResultRepository
import com.example.labresultviewer.repository.LabResultRepository
import com.example.labresultviewer.repository.PatientRepository
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
    @Provides
    @Singleton
    fun providePatientRepository(service: ApiService): PatientRepository {
        return PatientRepository(service)

    }
    @Provides
    @Singleton
    fun provideAdminAppointmentsRepository(service: AdminAppointmentService): AdminAppointmentsRepository {
        return AdminAppointmentsRepository(service)

    }
    @Provides
    @Singleton
    fun provideAdminLabResultRepository(service: AdminLabResultService): AdminLabResultRepository {
        return AdminLabResultRepository(service)
    }
}