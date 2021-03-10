package com.future.wanandroid.ui.user.opensource

import android.os.Bundle
import com.future.mvvmk.base.BaseActivity
import com.future.wanandroid.R
import com.future.wanandroid.bean.Article
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.details.DetailActivity.Companion.PARAM_ARTICLE
import kotlinx.android.synthetic.main.activity_open_source.*
import kotlinx.android.synthetic.main.include_title.*

class OpenSourceActivity : BaseActivity() {

    private val openSourceData = listOf(
        Article(
            title = "OkHttp",
            link = "https://github.com/square/okhttp"
        ),
        Article(
            title = "Retrofit",
            link = "https://github.com/square/retrofit"
        ),
        Article(
            title = "BaseRecyclerViewAdapterHelper",
            link = "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
        ),
        Article(
            title = "FlowLayout",
            link = "https://github.com/hongyangAndroid/FlowLayout"
        ),
        Article(
            title = "Banner",
            link = "https://github.com/youth5201314/banner"
        ),
        Article(
            title = "Glide",
            link = "https://github.com/bumptech/glide"
        ),
        Article(
            title = "Glide-Tansformations",
            link = "https://github.com/wasabeef/glide-transformations"
        ),
        Article(
            title = "AgentWeb",
            link = "https://github.com/Justson/AgentWeb"
        ),
        Article(
            title = "LiveEventBus",
            link = "https://github.com/JeremyLiao/LiveEventBus"
        ),
        Article(
            title = "PersistentCookieJar",
            link = "https://github.com/franmontiel/PersistentCookieJar"
        ),
        Article(
            title = "Toolkit",
            link = "https://github.com/futureyang/toolkit"
        )
    )

    override fun getLayoutId() = R.layout.activity_open_source

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OpenSourceAdapter().apply {
            bindToRecyclerView(recyclerView)
            setNewData(openSourceData)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(DetailActivity::class.java, mapOf(PARAM_ARTICLE to article))
            }
        }
        tvTitle.text = getString(R.string.my_open_source)
        ivBack.setOnClickListener { finish() }
    }
}
