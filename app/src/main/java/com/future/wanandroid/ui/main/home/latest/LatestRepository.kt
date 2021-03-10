package com.future.wanandroid.ui.main.home.latest

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/14
 *
 */

class LatestRepository @Inject constructor() {
    suspend fun getArticleList(page: Int) = ApiTool.apiService.getArticleList(page).apiData()
}