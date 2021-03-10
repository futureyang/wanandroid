package com.future.wanandroid.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by yangqc on 2020/12/24
 *
 */
@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "wanandroid.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideReadHistoryDao(database: AppDatabase): ReadHistoryDao {
        return database.readHistoryDao()
    }
}