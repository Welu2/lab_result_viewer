package com.example.labresultviewer.repository


import com.example.labresultviewer.model.DashboardStats
import com.example.labresultviewer.network.ApiService
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getDashboardStats(): DashboardStats = api.getDashboardStats()
}
