package com.future.wanandroid.ui.main.navigation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.xiaojianjun.wanandroid.model.bean.Navigation

class NavigationViewModle @ViewModelInject constructor(val repository: NavigationRepository) :
    BaseViewModel() {

    val banners = MutableLiveData<List<Banner>>()
    val navigations = MutableLiveData<List<Navigation>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getData() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                banners.value = repository.getBanners()
                navigations.value = repository.getNavigations()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = true
            })
    }
}