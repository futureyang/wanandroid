package com.future.wanandroid.ui.search.result

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * Created by yangqc on 2021/1/8
 * 搜索结果
 */
@AndroidEntryPoint
class SearchResultFragment : BaseVmFragment<SearchResultViewModel>() {

    companion object {
        fun newInstance() = SearchResultFragment()
    }

    private var position = 0

    private lateinit var mAdapter: ArticleAdapter

    override fun viewModelClass() = SearchResultViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_search_result

    override fun initView() {
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
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
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.search() }
        }
        btnReload.setOnClickListener {
            mViewModel.search()
        }
    }

    override fun observe() {
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(viewLifecycleOwner, Observer {
                emptyView.isVisible = it
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
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    fun doSearch(searchWords: String) {
        mViewModel.search(searchWords)
    }
}