package com.junlong0716.base.module.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.junlong0716.base.module.R


/**
 *@author: EdsionLi
 *@description: 加载弹窗
 *@date: Created in 2018/8/15 上午11:02
 *@modified by:
 */
class CommonLoadingDialog(activity: AppCompatActivity) : Dialog(activity, R.style.common_dialog_alpha_theme) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_loading)
        findViewById<LottieAnimationView>(R.id.loading_view).setAnimation("common_anim_loading.json")
    }
}