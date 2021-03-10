package com.future.wanandroid.ui.common

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

class CollectRepository @Inject constructor(){

    suspend fun collect(id: Int) = ApiTool.apiService.collect(id).apiData()

    suspend fun uncollect(id: Int) = ApiTool.apiService.uncollect(id).apiData()
}