package com.future.wanandroid.ui.details

import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.Tag
import com.future.wanandroid.db.ReadHistoryDao
import javax.inject.Inject

/**
 * Created by yangqc on 2020/12/29
 *
 */
class DetailRepository @Inject constructor() {

    @Inject
    lateinit var readHistoryDao: ReadHistoryDao

    suspend fun saveReadHistory(article: Article) = addReadHistory(article)

    suspend fun addReadHistory(article: Article) {
        readHistoryDao.queryArticle(article.id)?.let {
            readHistoryDao.deleteArticle(it)
        }
        readHistoryDao.insert(article.apply { primaryKeyId = 0 })
        article.tags.forEach {
            readHistoryDao.insertArticleTag(
                Tag(id = 0, articleId = article.id.toLong(), name = it.name, url = it.url)
            )
        }
    }
}