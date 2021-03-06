package com.future.wanandroid.db

import androidx.room.*
import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.ReadHistory
import com.future.wanandroid.bean.Tag

@Dao
interface ReadHistoryDao {

    @Transaction
    @Insert(entity = Article::class)
    suspend fun insert(article: Article): Long

    @Transaction
    @Insert(entity = Tag::class)
    suspend fun insertArticleTag(tag: Tag): Long

    @Transaction
    @Query("SELECT * FROM article")
    suspend fun queryAll(): List<ReadHistory>

    @Transaction
    @Query("SELECT * FROM article WHERE id = (:id)")
    suspend fun queryArticle(id: Int): Article?

    @Transaction
    @Delete(entity = Article::class)
    suspend fun deleteArticle(article: Article)

    @Transaction
    @Update(entity = Article::class)
    suspend fun updateArticle(article: Article)

}