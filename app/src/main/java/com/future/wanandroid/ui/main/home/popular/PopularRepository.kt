package com.future.wanandroid.ui.main.home.popular

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/14
 *
 */

class PopularRepository @Inject constructor(){

    suspend fun getTopArticleList() = ApiTool.apiService.getTopArticleList().apiData()

    suspend fun getArticleList(page: Int) = ApiTool.apiService.getArticleList(page).apiData()

}