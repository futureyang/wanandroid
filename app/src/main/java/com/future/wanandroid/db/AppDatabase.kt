package com.future.wanandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.Tag

@Database(entities = [Article::class, Tag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun readHistoryDao(): ReadHistoryDao
}