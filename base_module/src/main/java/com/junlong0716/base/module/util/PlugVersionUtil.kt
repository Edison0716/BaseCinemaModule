package com.junlong0716.base.module.util

import android.content.Context
import android.content.pm.PackageManager

/**
 * @author: EdsionLi
 * @description: 获取插件信息工具类
 * @date: Created in 2018/9/20 下午18:18
 * @modified by:
 */

object PlugVersionUtil {
    fun getPlugVersionCode(context: Context, plugPath: String): Int {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(plugPath, PackageManager.GET_ACTIVITIES)
        return info.versionCode
    }

    fun getPlugVersionName(context: Context, plugPath: String): String {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(plugPath, PackageManager.GET_ACTIVITIES)
        return info.versionName
    }

    fun getPlugPackageName(context: Context, plugPath: String): String {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(plugPath, PackageManager.GET_ACTIVITIES)
        return info.packageName
    }
}