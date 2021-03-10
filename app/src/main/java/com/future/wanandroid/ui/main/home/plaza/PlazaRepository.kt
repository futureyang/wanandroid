package com.future.wanandroid.ui.main.home.plaza

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/18
 *
 */
class PlazaRepository @Inject constructor() {
    suspend fun getUserArticleList(page: Int) = ApiTool.apiService.getUserArticleList(page).apiData()
}