package com.future.wanandroid.store

import com.future.wanandroid.util.getSpValue
import com.future.wanandroid.util.putSpValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object SearchHistoryStore {

    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchHistory"
    private val mGson by lazy { Gson() }

    fun saveSearchHistory(words: String) {
        val history = getSearchHistory()
        if (history.contains(words)) {
            history.remove(words)
        }
        history.add(0, words)
        val listStr = mGson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, listStr)
    }

    fun deleteSearchHistory(words: String) {
        val history = getSearchHistory()
        history.remove(words)
        val listStr = mGson.toJson(history)
        putSpValue(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, listStr)
    }

    fun getSearchHistory(): MutableList<String> {
        val listStr = getSpValue(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, "")
        return if (listStr.isEmpty()) {
            mutableListOf()
        } else {
            mGson.fromJson<MutableList<String>>(
                listStr,
                object : TypeToken<MutableList<String>>() {}.type
            )
        }
    }
}
