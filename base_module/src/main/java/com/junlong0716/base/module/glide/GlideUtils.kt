package com.junlong0716.base.module.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 *@author: 巴黎没有摩天轮Li
 *@description:
 *@date: Created in 下午12:55 2017/11/30
 *@modified by:
 */
object GlideUtils {

    fun showImage(context: Context, path: Any?, imageView: ImageView) {
        GlideApp.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun showImage(context: Context, path: Any?, imageView: ImageView, placeholder: Int) {
        GlideApp.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .placeholder(placeholder)
                .into(imageView)
    }

    fun showImage(context: Context, path: Any?, errorHolder: Int, imageView: ImageView) {
        GlideApp.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .error(errorHolder)
                .into(imageView)
    }

    fun showImageFade(context: Context, path: Any?, imageView: ImageView) {
        GlideApp.with(context).load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView)
    }

    fun showImageFade(context: Context, path: Any?, imageView: ImageView, placeholder: Int) {
        GlideApp.with(context).load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .placeholder(placeholder)
                .into(imageView)
    }


    fun showBlurImage(context: Context, path: Any?, imageView: ImageView, blurRadius: Int) {
        GlideApp.with(context).load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(bitmapTransform(BlurTransformation(blurRadius)))
                .into(imageView)
    }

    fun showBlurImageFade(context: Context, path: Any?, imageView: ImageView, blurRadius: Int) {
        GlideApp.with(context).load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(bitmapTransform(BlurTransformation(blurRadius)))
                .into(imageView)
    }

    fun showRoundImageFade(context: Context, path: Any?, imageView: ImageView, radius: Int, margin: Int, type: RoundedCornersTransformation.CornerType) {
        GlideApp.with(context)
                .load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, 0, type)))
                .into(imageView)
    }


    fun showRoundImageFade(context: Context, path: Any?, imageView: ImageView, radius: Int, margin: Int, type: RoundedCornersTransformation.CornerType,placeholder: Int) {
        GlideApp.with(context)
                .load(path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin, type)))
                .placeholder(placeholder)
                .into(imageView)
    }

    fun showRoundImage(context: Context, path: Any?, imageView: ImageView, radius: Int, margin: Int, type: RoundedCornersTransformation.CornerType,placeholder: Int) {
        GlideApp.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(radius, margin, type)))
                .placeholder(placeholder)
                .into(imageView)
    }
}