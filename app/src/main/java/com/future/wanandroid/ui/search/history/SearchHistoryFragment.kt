package com.future.wanandroid.ui.search.history

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.future.mvvmk.base.BaseVmFragment
import com.future.wanandroid.R
import com.future.wanandroid.bean.HotWord
import com.future.wanandroid.databinding.FragmentSearchHistoryBinding
import com.future.wanandroid.ui.search.SearchActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_hot_search.view.*

/**
 * Created by yangqc on 2021/1/8
 * 搜索历史
 */
@AndroidEntryPoint
class SearchHistoryFragment : BaseVmFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    lateinit var mAdapter: SearchHistoryAdapter

    override fun viewModelClass() = SearchHistoryViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_search_history

    override fun initView() {
        mAdapter = SearchHistoryAdapter(activity!!).apply {
            mBinding.recyclerView.adapter = this
            onItemClickListener = {
                (activity as? SearchActivity)?.fillSearchInput(data[it])
            }
            onDeleteClickListener = {
                mViewModel.deleteSearchHistory(mAdapter.data[it])
            }
        }
    }

    override fun initData() {
        mViewModel.getHotSearch()
        mViewModel.getSearchHistory()
    }

    override fun observe() {
        mBinding.viewModel = mViewModel
        mViewModel.run {
            hotWords.observe(viewLifecycleOwner, Observer {
                setHotwords(it)
            })
            searchHistory.observe(viewLifecycleOwner, Observer {
                mAdapter.submitList(it)
            })
        }
    }

    private fun setHotwords(hotwords: List<HotWord>) {
        mBinding.tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(hotwords) {
                override fun getView(parent: FlowLayout?, position: Int, hotWord: HotWord?): View {
                    return LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_hot_search, parent, false)
                        .apply { this.tvTag.text = hotWord?.name }
                }
            }
            setOnTagClickListener { _, position, _ ->
                (activity as? SearchActivity)?.fillSearchInput(hotwords[position].name)
                false
            }
        }
    }

    fun addSearchHistory(keywords: String) {
        mViewModel.addSearchHistory(keywords)
    }
}