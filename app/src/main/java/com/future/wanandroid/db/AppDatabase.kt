package com.future.wanandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.Tag
import com.future.wanandroid.bean.UserPhoto

@Database(entities = [Article::class, Tag::class, UserPhoto::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun readHistoryDao(): ReadHistoryDao

    abstract fun userInfoDao(): UserInfoDao
}