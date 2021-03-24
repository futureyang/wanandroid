package com.future.wanandroid.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        ).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideReadHistoryDao(database: AppDatabase): ReadHistoryDao {
        return database.readHistoryDao()
    }

    @Provides
    fun provideUserInfoDao(database: AppDatabase): UserInfoDao {
        return database.userInfoDao()
    }

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE UserPhoto (id INTEGER NOT NULL, photoUri TEXT NOT NULL, PRIMARY KEY(id))")
        }
    }

}