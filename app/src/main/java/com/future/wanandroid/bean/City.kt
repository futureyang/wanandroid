package com.future.wanandroid.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "citys")
class City (
    @SerializedName("id")
    @PrimaryKey
    val provinceCode: Int,
    @SerializedName("name")
    val provinceName: String
)