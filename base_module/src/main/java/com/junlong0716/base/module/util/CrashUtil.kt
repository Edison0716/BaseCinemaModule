package com.junlong0716.base.module.util

/**
 *@author: EdsionLi
 *@description: 用于制造一个崩溃
 *@date: Created in 2018/9/19 上午11:01
 *@modified by:
 */
object CrashUtil {
    private lateinit var mCrashList: ArrayList<Any>

    fun makeCrash() {
        mCrashList.add("制造崩溃")
    }
}