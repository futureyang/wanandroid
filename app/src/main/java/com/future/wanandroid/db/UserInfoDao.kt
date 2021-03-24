package com.future.wanandroid.db

import androidx.room.*
import com.future.wanandroid.bean.Article
import com.future.wanandroid.bean.UserPhoto

/**
 * Created by yangqc on 2021/3/23
 *
 */
@Dao
interface UserInfoDao {

    @Transaction
    @Query("SELECT * FROM userPhoto WHERE id = (:id)")
    suspend fun queryUserPhoto(id: Int): UserPhoto?

    @Transaction
    @Insert(entity = UserPhoto::class)
    suspend fun insertUserPhoto(userPhoto: UserPhoto): Long

    @Transaction
    @Delete(entity = UserPhoto::class)
    suspend fun deleteUserPhoto(userPhoto: UserPhoto)
}