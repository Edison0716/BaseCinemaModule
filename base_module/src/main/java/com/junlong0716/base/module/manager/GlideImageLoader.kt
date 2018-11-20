package com.junlong0716.base.module.manager

import android.content.Context
import android.widget.ImageView
import com.junlong0716.base.module.R
import com.junlong0716.base.module.glide.GlideUtils
import com.youth.banner.loader.ImageLoader

/**
 *@author: EdsionLi
 *@description: banner 图片加载管理器
 *@date: Created in 2018/3/16 下午4:42
 *@modified by:
 */
class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        imageView!!.scaleType = ImageView.ScaleType.FIT_XY
        GlideUtils.showImage(context!!, path, imageView, R.mipmap.common_ic_placeholder)
    }
}