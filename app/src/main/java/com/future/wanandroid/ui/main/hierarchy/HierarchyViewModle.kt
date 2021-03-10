package com.future.wanandroid.ui.main.hierarchy

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.Category

class HierarchyViewModle @ViewModelInject constructor(val repository: HierarchyRepository) : BaseViewModel() {

    val categories = MutableLiveData<MutableList<Category>>()
    val loadingStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getArticleCategory() {
        loadingStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                categories.value = repository.getArticleCategories()
                loadingStatus.value = false
            },
            error = {
                loadingStatus.value = false
                reloadStatus.value = true
            }
        )
    }
}