package com.future.wanandroid.ui.main.home.questions

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.common.bus.LiveBus
import com.future.wanandroid.common.ScrollToTop
import com.future.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.future.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.future.wanandroid.ui.ActivityManager
import com.future.wanandroid.ui.details.DetailActivity
import com.future.wanandroid.ui.main.adapter.ArticleAdapter
import com.future.wanandroid.common.loadmore.CommonLoadMoreView
import com.future.wanandroid.common.loadmore.LoadMoreStatus
import com.future.wanandroid.databinding.FragmentQuestionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.include_reload.view.*

/**
 * 问答
 */
@AndroidEntryPoint
class QuestionsFragment : BaseVmFragment<QuestionsViewModel, FragmentQuestionsBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = QuestionsFragment()
    }

    private var position = 0

    private lateinit var mAdapter: ArticleAdapter

    override fun viewModelClass() = QuestionsViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_questions

    override fun initView() {
        mBinding.resId = R.color.textColorPrimary
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMore()
            }, mBinding.recyclerView)
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
        (mBinding.recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refresh()
        }
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
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
        mViewModel.refresh()
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}