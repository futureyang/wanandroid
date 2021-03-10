package com.future.wanandroid.ui.user.share.list

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/2/3
 *
 */
class ShareListRepository @Inject constructor() {

    suspend fun getSharedArticleList(page: Int) = ApiTool.apiService.getSharedArticleList(page).apiData()

    suspend fun deleteShared(id: Int) = ApiTool.apiService.deleteShare(id).apiData()
}