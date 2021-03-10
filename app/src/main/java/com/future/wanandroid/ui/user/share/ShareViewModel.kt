package com.future.wanandroid.ui.user.share

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.UserInfo

/**
 * Created by yangqc on 2021/2/3
 *
 */
class ShareViewModel @ViewModelInject constructor(val repository: ShareRepository) : BaseViewModel() {

    val userInfo = MutableLiveData<UserInfo>()
    val submitting = MutableLiveData<Boolean>()
    val shareResult = MutableLiveData<Boolean>()

    fun getUserInfo() {
        userInfo.value = userRepository.getUserInfo()
    }

    fun shareArticle(title: String, link: String) {
        submitting.value = true
        launch(
            block = {
                repository.shareArticle(title, link)
                shareResult.value = true
                submitting.value = false
            },
            error = {
                shareResult.value = false
                submitting.value = false
            }
        )
    }

}