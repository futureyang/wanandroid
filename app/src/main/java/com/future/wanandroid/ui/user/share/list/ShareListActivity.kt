package com.future.wanandroid.ui.user.share.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.ui.user.share.ShareActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_share_list.emptyView
import kotlinx.android.synthetic.main.activity_share_list.recyclerView
import kotlinx.android.synthetic.main.activity_share_list.reloadView
import kotlinx.android.synthetic.main.activity_share_list.swipeRefreshLayout
import kotlinx.android.synthetic.main.include_reload.*
import kotlinx.android.synthetic.main.include_title.*


/**
 * Created by yangqc on 2021/2/3
 *
 */
@AndroidEntryPoint
class ShareListActivity : BaseVmActivity<ShareListViewModel>() {

    lateinit var mAdapter : ArticleAdapter

    private var position = 0

    override fun viewModelClass() = ShareListViewModel::class.java

    override fun getLayoutId() = R.layout.activity_share_list


    override fun initView(savedInstanceState: Bundle?) {
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
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
            setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@ShareListActivity)
                    .setMessage(R.string.confirm_delete_shared)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.deleteShared(data[position].id)
                        mAdapter.remove(position)
                        this@ShareListActivity.emptyView.isVisible = data.isEmpty()
                    }.show()
                true
            }
            setOnLoadMoreListener({ mViewModel.loadMore() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.loadMore() }
        }
        btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        tvTitle.text = getString(R.string.my_share)
        ivBack.setOnClickListener { finish() }
        ivMenu.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_add_black_24dp)
            setOnClickListener {
                ActivityManager.start(ShareActivity::class.java)
            }
        }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        mViewModel.run {
            articleList.observe(this@ShareListActivity, Observer {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(this@ShareListActivity, Observer {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@ShareListActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@ShareListActivity, Observer {
                reloadView.isVisible = it
            })
            emptyStatus.observe(this@ShareListActivity, Observer {
                emptyView.isVisible = it
            })
        }
        LiveBus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        LiveBus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }

}