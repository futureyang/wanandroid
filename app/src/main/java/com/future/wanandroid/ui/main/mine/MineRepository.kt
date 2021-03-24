package com.future.wanandroid.ui.main.mine

import com.future.wanandroid.bean.UserPhoto
import com.future.wanandroid.db.UserInfoDao
import javax.inject.Inject

/**
 * Created by yangqc on 2021/3/23
 *
 */
class MineRepository @Inject constructor() {

    @Inject
    lateinit var userInfoDao: UserInfoDao

    suspend fun getUserPhoto(id: Int) = userInfoDao.queryUserPhoto(id)?.photoUri

    suspend fun saveUserPhoto(userPhoto: UserPhoto) {
        userInfoDao.deleteUserPhoto(userPhoto)
        userInfoDao.insertUserPhoto(userPhoto)
    }
}