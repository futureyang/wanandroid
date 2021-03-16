package com.future.wanandroid.ui.user.ranking

import android.os.Bundle
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.ActivityRankingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/1/28
 * 积分排行
 */
@AndroidEntryPoint
class RankingActivity : BaseVmActivity<RankingViewModel, ActivityRankingBinding>() {

    override fun viewModelClass() = RankingViewModel::class.java

    override fun getLayoutId() = R.layout.activity_ranking

    lateinit var mAdapter: RankingAdapter

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.resId = R.color.textColorPrimary
        mAdapter = RankingAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMore() }, mBinding.recyclerView)
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
        mBinding.viewModel = mViewModel
        mViewModel.run {
            rankingList.observe(this@RankingActivity, Observer {
                mAdapter.setNewData(it)
            })
            loadMoreStatus.observe(this@RankingActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
        }
    }
}