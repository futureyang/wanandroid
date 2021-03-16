package com.future.wanandroid.ui.user.integral

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.ActivityIntegralBinding
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.user.ranking.RankingActivity
import com.future.wanandroid.util.doIntAnim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.header_mine_points.view.*
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/1/28
 * 我的积分
 */
@AndroidEntryPoint
class IntegralActivity : BaseVmActivity<IntegralViewModel, ActivityIntegralBinding>() {

    lateinit var mAdapter: IntegralAdapter
    private lateinit var mHeaderView: View

    override fun viewModelClass() = IntegralViewModel::class.java

    override fun getLayoutId() = R.layout.activity_integral

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.resId = R.color.bgColorPrimary
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_mine_points, null)
        mAdapter = IntegralAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMoreRecord() }, mBinding.recyclerView)
        }
        btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        tvTitle.text = getString(R.string.my_points)
        ivBack.setOnClickListener { finish() }
        ivMenu.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_graphic_eq_black_24dp)
            setOnClickListener {
                ActivityManager.start(RankingActivity::class.java)
            }
        }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            totalPoints.observe(this@IntegralActivity, Observer {
                if (mAdapter.headerLayoutCount == 0) {
                    mAdapter.setHeaderView(mHeaderView)
                }
                doIntAnim(mHeaderView.tvTotalPoints, it.coinCount, 2000)
                mHeaderView.tvLevelRank.text = getString(R.string.level_rank, it.level, it.rank)

            })
            pointsList.observe(this@IntegralActivity, Observer {
                mAdapter.setNewData(it)
            })
            loadMoreStatus.observe(this@IntegralActivity, Observer {
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