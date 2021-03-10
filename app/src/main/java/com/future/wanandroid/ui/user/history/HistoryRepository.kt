package com.future.wanandroid.ui.user.history

import com.future.wanandroid.bean.Article
import com.future.wanandroid.db.ReadHistoryDao
import javax.inject.Inject

/**
 * Created by yangqc on 2021/1/18
 *
 */
class HistoryRepository @Inject constructor() {

    @Inject
    lateinit var readHistoryDao: ReadHistoryDao

    suspend fun getReadHistory() =
        readHistoryDao.queryAll().map { it.article.apply { tags = it.tags } }.reversed()

    suspend fun deleteHistory(article: Article) = readHistoryDao.deleteArticle(article)
}