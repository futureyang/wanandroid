package com.future.wanandroid.ui.user.share

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2021/2/3
 *
 */
class ShareRepository @Inject constructor() {
    suspend fun shareArticle(title: String, link: String) =
        ApiTool.apiService.shareArticle(title, link).apiData()
}