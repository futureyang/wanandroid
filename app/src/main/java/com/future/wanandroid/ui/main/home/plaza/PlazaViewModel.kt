package com.future.wanandroid.ui.main.home.plaza

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.Article
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.ui.common.CollectRepository
import com.future.wanandroid.common.loadmore.LoadMoreStatus

class PlazaViewModel @ViewModelInject constructor(
    val plazaRepository: PlazaRepository,
    val collectRepository: CollectRepository
) : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private var page = INITIAL_PAGE

    val articleList = MutableLiveData<MutableList<Article>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>() //正在刷新
    val reloadStatus = MutableLiveData<Boolean>() //

    fun refresh() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val pagination = plazaRepository.getUserArticleList(INITIAL_PAGE)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMore() {
        launch({
            val articleData = plazaRepository.getUserArticleList(page)
            page = articleData.curPage
            val currentList = articleList.value ?: mutableListOf()
            currentList.addAll(articleData.datas)
            articleList.value = currentList
            loadMoreStatus.value = if (articleData.offset >= articleData.total) {
                LoadMoreStatus.END
            } else {
                LoadMoreStatus.COMPLETED
            }
        }, error = {
            loadMoreStatus.value = LoadMoreStatus.ERROR
        })
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
}