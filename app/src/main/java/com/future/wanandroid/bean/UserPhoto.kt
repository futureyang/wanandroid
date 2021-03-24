package com.future.wanandroid.bean

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

/**
 * Created by yangqc on 2021/3/23
 *
 */
@Parcelize
@Entity
data class UserPhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val photoUri: String
): Parcelable