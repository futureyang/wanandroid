package com.future.wanandroid.ui.login.login

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/18
 *
 */
class LoginRepository @Inject constructor() {

    suspend fun login(username: String, password: String) = ApiTool.apiService.login(username, password).apiData()
}