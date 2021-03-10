package com.future.wanandroid.ui.search.history

import com.future.wanandroid.network.ApiTool
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStore
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/8
 *
 */
class SearchHistoryRepository@Inject constructor() {

    suspend fun getHotSearch() = ApiTool.apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

    fun getSearchHisory() = SearchHistoryStore.getSearchHistory()
}