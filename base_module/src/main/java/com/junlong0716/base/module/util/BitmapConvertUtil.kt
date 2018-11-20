package com.junlong0716.base.module.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.widget.ScrollView


/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/4/28 下午1:28
 *@modified by:
 */
object BitmapConvertUtil{
    /**
     * 截取scrollview的屏幕
     * @param scrollView
     * @return
     */
    fun getBitmapByView(scrollView: ScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        // 获取scrollview实际高度
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"))
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.width, h,
                Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }
}