package com.example.labresultviewer.di

import com.example.labresultviewer.network.AdminAppointmentService
import com.example.labresultviewer.network.AdminLabResultService
import com.example.labresultviewer.network.ApiService
import com.example.labresultviewer.network.AuthService
import com.example.labresultviewer.network.LabResultService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs full request and response
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.100.7:3001")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    @Provides
    @Singleton
    fun provideLabResultService(retrofit: Retrofit): LabResultService {
        return retrofit.create(LabResultService::class.java)
    }
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminAppointmentService(retrofit: Retrofit): AdminAppointmentService {
        return retrofit.create(AdminAppointmentService::class.java)

    }
    @Provides
    @Singleton
    fun provideAdminLabResultService(retrofit: Retrofit): AdminLabResultService {
        return retrofit.create(AdminLabResultService::class.java)
    }
}