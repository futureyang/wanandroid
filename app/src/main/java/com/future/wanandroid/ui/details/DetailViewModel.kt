package com.future.wanandroid.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.Article
import com.future.wanandroid.ui.common.CollectRepository

/**
 * Created by yangqc on 2020/12/29
 *
 */
class DetailViewModel @ViewModelInject constructor(
    val detailRepository: DetailRepository,
    val collectRepository: CollectRepository
) : BaseViewModel() {

    val collect = MutableLiveData<Boolean>()

    fun saveReadHistory(article: Article) {
        launch(block = { detailRepository.saveReadHistory(article) })
    }

    fun collect(id: Int, isCollect: Boolean) {
        launch({
            if (isCollect) {
                collectRepository.uncollect(id)
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
            } else {
                collectRepository.collect(id)
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
            }
            collect.value = !isCollect
        }, {
            collect.value = isCollect
        })
    }

    fun updateCollectStatus(id: Int) {
        collect.value = if (userRepository.isLogin()) {
            userRepository.getUserInfo()!!.collectIds.contains(id)
        } else {
            false
        }
    }
}