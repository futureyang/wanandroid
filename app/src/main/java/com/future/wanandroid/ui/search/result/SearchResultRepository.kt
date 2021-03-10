package com.future.wanandroid.ui.search.result

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/8
 *
 */
class SearchResultRepository @Inject constructor() {

    suspend fun search(keywords: String, page: Int) =
        ApiTool.apiService.search(keywords, page).apiData()
}