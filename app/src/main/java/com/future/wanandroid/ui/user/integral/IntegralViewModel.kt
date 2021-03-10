package com.future.wanandroid.ui.user.integral

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.PointRank
import com.future.wanandroid.bean.PointRecord
import com.future.wanandroid.common.loadmore.LoadMoreStatus

/**
 * Created by yangqc on 2021/1/28
 *
 */
class IntegralViewModel @ViewModelInject constructor(val repository: IntegralRepository) : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 1
    }

    val totalPoints = MutableLiveData<PointRank>() //我的积分信息
    val pointsList = MutableLiveData<MutableList<PointRecord>>() //积分变化

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    var page = INITIAL_PAGE

    fun refresh() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val points = repository.getMyPoints()
                val pagination = repository.getPointsRecord(INITIAL_PAGE)
                page = pagination.curPage
                totalPoints.value = points
                pointsList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMoreRecord() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = repository.getPointsRecord(page + 1)
                page = pagination.curPage
                pointsList.value?.addAll(pagination.datas)
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