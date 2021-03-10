package com.future.wanandroid.ui.user.history

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmActivity
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.include_title.*

/**
 * Created by yangqc on 2021/1/18
 *
 */
@AndroidEntryPoint
class HistoryActivity : BaseVmActivity<HistoryViewModel>() {

    private var position = 0

    private lateinit var mAdapter: ArticleAdapter

    override fun viewModelClass() = HistoryViewModel::class.java

    override fun getLayoutId() = R.layout.activity_history

    override fun initView(savedInstanceState: Bundle?) {
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
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
            setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@HistoryActivity)
                    .setMessage(R.string.confirm_delete_history)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.deleteHistory(data[position])
                        mAdapter.remove(position)
                        this@HistoryActivity.emptyView.isVisible = data.isEmpty()
                    }.show()
                true
            }
        }
        tvTitle.text = getString(R.string.my_view_history)
        ivBack.setOnClickListener { ActivityManager.finish(HistoryActivity::class.java) }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getData()
    }

    override fun observe() {
        mViewModel.run {
            articleList.observe(this@HistoryActivity, Observer {
                mAdapter.setNewData(it)
            })
            emptyStatus.observe(this@HistoryActivity, Observer {
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