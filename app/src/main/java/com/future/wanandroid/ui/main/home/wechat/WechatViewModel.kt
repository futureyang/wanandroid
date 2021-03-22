package com.future.wanandroid.ui.main.home.wechat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.Category
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.ui.common.CollectRepository
import com.future.wanandroid.common.loadmore.LoadMoreStatus

class WechatViewModel @ViewModelInject constructor(
    val wechattRepository: WechatRepository,
    val collectRepository: CollectRepository
) : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
        const val INITIAL_CHECKED = 0
    }

    private var page = INITIAL_PAGE + 1

    val categories = MutableLiveData<MutableList<Category>>() //项目分类
    val checkedCategory = MutableLiveData<Int>() //选择项目分类
    val articleList = MutableLiveData<MutableList<Article>>() //列表数据
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>() //加载更多
    val refreshStatus = MutableLiveData<Boolean>() //正在刷新
    val reloadStatus = MutableLiveData<Boolean>() //显示项目分类重新加载
    val reloadListStatus = MutableLiveData<Boolean>() //显示列表数据重新下载

    fun getWechatCategory() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val categoryList = wechattRepository.getWechatCategories()
                val checkedPosition = INITIAL_CHECKED
                val cid = categoryList[checkedPosition].id
                val pagination = wechattRepository.getWechatArticleList(INITIAL_PAGE, cid)
                page = pagination.curPage

                categories.value = categoryList
                checkedCategory.value = checkedPosition
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = true
            }
        )
    }

    fun refresh(checkedPosition: Int = checkedCategory.value ?: INITIAL_CHECKED) {
        refreshStatus.value = true
        reloadStatus.value = false
        if (checkedPosition != checkedCategory.value) {
            articleList.value = mutableListOf()
            checkedCategory.value = checkedPosition
        }
        launch(
            block = {
                val categoryList = categories.value ?: return@launch
                val cid = categoryList[checkedPosition].id
                val articleData = wechattRepository.getWechatArticleList(INITIAL_PAGE, cid)
                page = articleData.curPage
                articleList.value = articleData.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadListStatus.value = articleList.value?.isEmpty()
            }
        )
    }

    fun loadMore() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val categoryList = categories.value ?: return@launch
                val checkedPosition = checkedCategory.value ?: return@launch
                val cid = categoryList[checkedPosition].id
                val pagination = wechattRepository.getWechatArticleList(page + 1, cid)
                page = pagination.curPage

                val currentList = articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)

                articleList.value = currentList
                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
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
}