package com.future.wanandroid.ui.user.integral

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/28
 *
 */
class IntegralRepository @Inject constructor() {

    suspend fun getMyPoints() = ApiTool.apiService.getPoints().apiData()
    suspend fun getPointsRecord(page: Int) =
        ApiTool.apiService.getPointsRecord(page).apiData()
}