package com.gadgetmart.ui.dashboard

import com.gadgetmart.base.BaseContract


interface DashboardContract : BaseContract {

    fun onDashboardDataFound(dashboardResult: DashboardResult?, message: String)

    fun onDashboardDataNotFound(message: String)
}