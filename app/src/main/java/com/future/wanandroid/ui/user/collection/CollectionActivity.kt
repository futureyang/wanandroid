package com.future.wanandroid.ui.user.collection

import android.os.Bundle
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.ActivityCollectionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/1/14
 * 我的收藏
 */
@AndroidEntryPoint
class CollectionActivity : BaseVmActivity<CollectionViewModel, ActivityCollectionBinding>() {


    private lateinit var mAdapter: ArticleAdapter

    override fun viewModelClass() = CollectionViewModel::class.java

    override fun getLayoutId() = R.layout.activity_collection

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.resId = R.color.textColorPrimary
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect) {
                    mViewModel.uncollect(article.originId)
                    removeItem(position)
                }
            }
            setOnLoadMoreListener({ mViewModel.loadMore() }, mBinding.recyclerView)
        }
        btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        tvTitle.text = getString(R.string.my_collect)
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            articleList.observe(this@CollectionActivity, Observer {
                mAdapter.setNewData(it)
            })
            loadMoreStatus.observe(this@CollectionActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this) { (id, collect) ->
            if (collect) {
                mViewModel.refresh()
            } else {
                val position = mAdapter.data.indexOfFirst { it.id == id }
                if (position != -1) {
                    removeItem(position)
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        mAdapter.remove(position)
        mViewModel.emptyStatus.value = mAdapter.data.isEmpty()
    }

}