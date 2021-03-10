package com.future.wanandroid.ui.main.hierarchy.category

import android.view.LayoutInflater
import android.view.View
import com.future.wanandroid.R
import com.future.wanandroid.bean.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_system_category_tag.view.*

class ItemTagAdapter(private val categoryList: List<Category>) :
    TagAdapter<Category>(categoryList) {
    override fun getView(parent: FlowLayout?, position: Int, category: Category?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_system_category_tag, parent, false)
            .apply {
                tvTag.text = categoryList[position].name
            }
    }

    override fun setSelected(position: Int, t: Category?): Boolean {
        return super.setSelected(position, t)
    }
}