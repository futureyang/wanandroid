package com.future.wanandroid.ui.main.home.wechat

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/18
 *
 */
class WechatRepository @Inject constructor() {

    suspend fun getWechatCategories() = ApiTool.apiService.getWechatCategories().apiData()

    suspend fun getWechatArticleList(page: Int, cid: Int) = ApiTool.apiService.getWechatArticleList(page, cid).apiData()}