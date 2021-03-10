package com.future.wanandroid.bean

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    var primaryKeyId: Int = 0,
    var apkLink: String? = "",
    var audit: Int = 0,
    var author: String? = "", //作者
    var chapterId: Int = 0,
    var chapterName: String? = "", //章节名
    var collect: Boolean = false, //收藏
    var courseId: Int = 0, //课程id
    var desc: String? = "",
    var envelopePic: String? = "",
    var fresh: Boolean = false, //新的
    var id: Int = 0,
    var link: String? = "", //文章地址
    var niceDate: String? = "",
    var niceShareDate: String? = "",
    var origin: String? = "",
    var originId: Int = 0,
    var prefix: String? = "",
    var projectLink: String? = "",
    var publishTime: Long = 0,
    var selfVisible: Int = 0,
    var shareDate: Long = 0,
    var shareUser: String? = "", //分享人
    var superChapterId: Int = 0,//顶级章节id
    var superChapterName: String? = "", //顶级章节
    @Ignore
    var tags: List<Tag> = emptyList(),
    var title: String? = "", //文章标题
    var type: Int = 0,
    var userId: Int = 0,
    var visible: Int = 0, //可见
    var zan: Int = 0,
    var top: Boolean = false //置顶
) : Parcelable