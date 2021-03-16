package com.future.wanandroid.ui.login

import com.future.wanandroid.bean.UserInfo
import com.future.wanandroid.network.ApiTool
import com.future.wanandroid.store.UserInfoStore

open class UserRepository {

    fun updateUserInfo(userInfo: UserInfo) = UserInfoStore.setUserInfo(userInfo)

    fun isLogin() = UserInfoStore.isLogin()

    fun getUserInfo() = UserInfoStore.getUserInfo()

    fun clearLoginState() {
        UserInfoStore.clearUserInfo()
        ApiTool.clearCookie()
    }

}