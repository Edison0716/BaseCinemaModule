package com.junlong0716.base.module.rvdecoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *@author: 巴黎没有摩天轮Li
 *@description:
 *@date: Created in 上午8:46 2018/1/10
 *@modified by:
 */
class SpaceItemDecoration(space:Int) :RecyclerView.ItemDecoration(){
    private var mSpace:Int = space
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mSpace
    }
}