package com.future.wanandroid.ui.main.home.wechat

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.CategoryAdapter
import com.future.wanandroid.ui.main.adapter.SimpleArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*
import kotlinx.android.synthetic.main.layout_common_recyclerview_double.*

/**
 * 公众号
 */
@AndroidEntryPoint
class WechatFragment : BaseVmFragment<WechatViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = WechatFragment()
    }

    private var position = 0

    private lateinit var mAdapter: SimpleArticleAdapter

    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun viewModelClass() = WechatViewModel::class.java

    override fun getLayoutId() = R.layout.layout_common_recyclerview_double

    override fun initView() {
        swipe_refresh.apply {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshWechatArticleList() }
        }
        mCategoryAdapter = CategoryAdapter().apply {
            bindToRecyclerView(rvCategory)
            onCheckedListener = {
                mViewModel.refreshWechatArticleList(it)
            }
        }
        mAdapter = SimpleArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMoreWechatArticleList()
            }, recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = mAdapter.data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, i ->
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    position = i
                    val article = mAdapter.data[i]
                    mViewModel.collect(article.id, article.collect)
                }
            }
        }
        reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshWechatArticleList()
        }
        reloadView.btnReload.setOnClickListener {
            mViewModel.getWechatCategory()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            categories.observe(viewLifecycleOwner, Observer {
                rvCategory.isGone = it.isEmpty()
                mCategoryAdapter.setNewData(it)
            })
            checkedCategory.observe(viewLifecycleOwner, Observer {
                mCategoryAdapter.check(it)
            })
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                swipe_refresh.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                reloadView.isVisible = it
            })
            reloadListStatus.observe(viewLifecycleOwner, Observer {
                reloadListView.isVisible = it
            })
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.getWechatCategory()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}