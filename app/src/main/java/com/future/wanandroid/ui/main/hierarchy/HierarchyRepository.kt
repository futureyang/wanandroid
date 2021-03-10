package com.future.wanandroid.ui.main.hierarchy

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/29
 *
 */
class HierarchyRepository @Inject constructor() {

    suspend fun getArticleCategories() = ApiTool.apiService.getArticleCategories().apiData()
}