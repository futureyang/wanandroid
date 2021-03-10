package com.future.wanandroid.ui.main.home.project

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/18
 *
 */
class ProjectRepository @Inject constructor() {
    suspend fun getProjectCategories() = ApiTool.apiService.getProjectCategories().apiData()

    suspend fun getProjectArticleList(page: Int, cid: Int) = ApiTool.apiService.getProjectArticleList(page, cid).apiData()
}