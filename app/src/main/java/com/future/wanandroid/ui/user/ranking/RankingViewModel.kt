package com.future.wanandroid.ui.user.ranking

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.PointRank
import com.future.wanandroid.common.loadmore.LoadMoreStatus

/**
 * Created by yangqc on 2021/1/28
 *
 */
class RankingViewModel @ViewModelInject constructor(val respository : RankingRepository) : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 1
    }

    private var page = INITIAL_PAGE

    val rankingList = MutableLiveData<MutableList<PointRank>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun refresh() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val pagination = respository.getPointsRank(INITIAL_PAGE)
                page = pagination.curPage
                rankingList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMore() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = respository.getPointsRank(page + 1)
                page = pagination.curPage
                rankingList.value?.addAll(pagination.datas)
                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = { loadMoreStatus.value = LoadMoreStatus.ERROR }
        )
    }
}