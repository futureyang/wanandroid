package com.future.wanandroid.ui.search.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.future.mvvmk.base.BaseViewModel
import com.future.wanandroid.bean.HotWord

/**
 * Created by yangqc on 2021/1/8
 *
 */
class SearchHistoryViewModel @ViewModelInject constructor(val repository: SearchHistoryRepository) : BaseViewModel() {

    val hotWords = MutableLiveData<List<HotWord>>()
    val searchHistory = MutableLiveData<MutableList<String>>()

    fun getHotSearch() {
        launch(block = { hotWords.value = repository.getHotSearch() })
    }

    fun getSearchHistory() {
        searchHistory.value = repository.getSearchHisory()
    }

    fun addSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
        }
        history.add(0, searchWords)
        searchHistory.value = history
        repository.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
            searchHistory.value = history
            repository.deleteSearchHistory(searchWords)
        }
    }
}