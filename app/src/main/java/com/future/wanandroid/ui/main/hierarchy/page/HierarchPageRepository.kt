package com.future.wanandroid.ui.main.hierarchy.page

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/31
 *
 */
class HierarchPageRepository @Inject constructor() {

    suspend fun getArticleListByCid(page: Int, cid: Int) = ApiTool.apiService.getArticleListByCid(page, cid).apiData()
}