package com.future.wanandroid.ui.main.navigation

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/8
 *
 */
class NavigationRepository@Inject constructor() {

    suspend fun getBanners() = ApiTool.apiService.getBanners().apiData()

    suspend fun getNavigations() = ApiTool.apiService.getNavigations().apiData()
}