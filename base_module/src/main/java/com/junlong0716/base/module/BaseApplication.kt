package com.junlong0716.base.module

import android.app.Application
import com.blankj.utilcode.util.Utils


/**
 *@author: EdsionLi
 *@description: 基类Application
 *@date: Created in 2018/8/2 下午5:02
 *@modified by:
 */
abstract class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //初始化工具类
        Utils.init(this)
    }
}