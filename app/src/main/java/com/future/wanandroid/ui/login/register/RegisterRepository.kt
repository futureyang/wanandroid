package com.future.wanandroid.ui.login.register

import com.future.wanandroid.network.ApiTool
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/18
 *
 */
class RegisterRepository @Inject constructor() {
    suspend fun register(username: String, password: String, confirmPassword: String) = ApiTool.apiService.register(username, password, confirmPassword).apiData()
}