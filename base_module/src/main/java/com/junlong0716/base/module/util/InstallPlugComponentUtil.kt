package com.junlong0716.base.module.util

import android.content.Context
import com.didi.virtualapk.PluginManager
import com.orhanobut.logger.Logger
import java.io.File

/**
 *@author: EdsionLi
 *@description: 安装插件工具类
 *@date: Created in 2018/8/10 上午10:13
 *@modified by:
 */
object InstallPlugComponentUtil {
    fun installPlugComponent(context: Context, componentSaveDirName: String, componentName: String, componentPackageName: String, installPlugInComponentCallback: InstallPlugInComponentCallback) {
        val componentPath = context.getExternalFilesDir(componentSaveDirName).absolutePath + File.separator + componentName
        val componentFile = File(componentPath)
        //插件不存在
        if (!componentFile.exists()) {
            Logger.e(componentFile.path)
            installPlugInComponentCallback.onComponentNonExist()
            return
        }

        //判断插件是否存在
        if (PluginManager.getInstance(context).getLoadedPlugin(componentPackageName) == null) {

            try {
                //加载插件包
                PluginManager.getInstance(context).loadPlugin(componentFile)
            } catch (e: Exception) {
                //加载插件包 失败
                installPlugInComponentCallback.onComponentInstallFailed()
                return
            }
        }

        installPlugInComponentCallback.onComponentHasLoaded()
    }

    interface InstallPlugInComponentCallback {
        //插件包不存在
        fun onComponentNonExist()

        //插件加载完成
        fun onComponentHasLoaded()

        //加载插件失败
        fun onComponentInstallFailed()
    }
}