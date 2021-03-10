package com.future.wanandroid.ui.user.collection

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/14
 *
 */
class CollectionRepository@Inject constructor() {

    suspend fun getCollectionList(page: Int) =
        ApiTool.apiService.getCollectionList(page).apiData()
}