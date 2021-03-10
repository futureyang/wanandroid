package com.future.wanandroid.ui.main.home.questions

import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.network.service.ApiService
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/14
 *
 */

class QuestionsRepository @Inject constructor() {
    suspend fun getQuestionsList(page: Int) = ApiTool.apiService.getQuestionsList(page).apiData()
}