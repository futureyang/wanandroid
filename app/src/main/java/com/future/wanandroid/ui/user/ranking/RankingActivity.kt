package com.future.wanandroid.ui.user.ranking

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/1/28
 *
 */
@AndroidEntryPoint
class RankingActivity : BaseVmActivity<RankingViewModel>() {

    override fun viewModelClass() = RankingViewModel::class.java

    override fun getLayoutId() = R.layout.activity_ranking

    lateinit var mAdapter: RankingAdapter

    override fun initView(savedInstanceState: Bundle?) {
        mAdapter = RankingAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMore() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        tvTitle.text = getString(R.string.my_points_rank)
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        mViewModel.run {
            rankingList.observe(this@RankingActivity, Observer {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(this@RankingActivity, Observer {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@RankingActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@RankingActivity, Observer {
                reloadView.isVisible = it
            })
        }
    }
}