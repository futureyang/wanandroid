package com.future.wanandroid.ui.user.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.ui.common.CollectRepository

/**
 * Created by yangqc on 2021/1/18
 *
 */
class HistoryViewModel @ViewModelInject constructor(
    val historyRepository: HistoryRepository,
    val collectRepository: CollectRepository
) : BaseViewModel() {

    val articleList = MutableLiveData<MutableList<Article>>()
    val emptyStatus = MutableLiveData<Boolean>()

    fun getData() {
        emptyStatus.value = false
        launch(
            block = {
                val readHistory = historyRepository.getReadHistory()
                val collectIds = userRepository.getUserInfo()?.collectIds ?: emptyList<Int>()
                // 更新收藏状态
                readHistory.forEach { it.collect = collectIds.contains(it.id) }

                articleList.value = readHistory.toMutableList()
                emptyStatus.value = readHistory.isEmpty()
            }
        )
    }

    fun collect(id: Int, isCollect: Boolean) {
        launch({
            if (isCollect) {
                collectRepository.uncollect(id)
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
            } else {
                collectRepository.collect(id)
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
            }
            updateItemCollectState(id to !isCollect)
            LiveBus.post(USER_COLLECT_UPDATED, id to !isCollect)
        }, {
            LiveBus.post(USER_COLLECT_UPDATED, id to isCollect)
        })
    }

    /**
     * 登录状态改变
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (userRepository.isLogin()) {
            val collectIds = userRepository.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Int, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }

    fun deleteHistory(article: Article) {
        launch(
            block = { historyRepository.deleteHistory(article) }
        )
    }
}