package com.future.wanandroid.ui.main.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.toolkit.utils.log.LogUtils
import com.future.wanandroid.bean.UserInfo
import com.future.wanandroid.bean.UserPhoto

class MineViewModle @ViewModelInject constructor(val repository: MineRepository) : BaseViewModel() {

    val userInfo = MutableLiveData<UserInfo?>()
    val isLogin = MutableLiveData<Boolean>()
    val photoUri = MutableLiveData<String>()

    fun getUserInfo() {
        isLogin.value = userRepository.isLogin()
        userInfo.value = userRepository.getUserInfo()
        if (isLogin.value!!) {
            launch(
                block = {
                    photoUri.value = repository.getUserPhoto(userInfo.value!!.id)
                    LogUtils.d("ImageUrl", photoUri.value)
                }
            )
        } else {
            photoUri.value = ""
        }
    }

    fun saveUserPhoto(uri: String) {
        launch(
            block = {
                val id = repository.saveUserPhoto(UserPhoto(userInfo.value!!.id, uri))
                photoUri.value = uri
            }
        )
    }

}