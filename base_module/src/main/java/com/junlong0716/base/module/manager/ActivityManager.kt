package com.junlong0716.base.module.manager

import android.content.Context

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

import java.util.Stack

/**
 * @author: EdsionLi
 * @description: 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @date: Created in 2018/3/14 下午4:47
 * @modified by:
 */

object ActivityManager {
    private val activityStack = Stack<RxAppCompatActivity>()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: RxAppCompatActivity) {
        activityStack.push(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): RxAppCompatActivity {
        return activityStack.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurrentActivity() {
        val activity = activityStack.pop()
        activity.finish()
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: RxAppCompatActivity?) {
        if (activity != null) {
            activityStack.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity?.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val manager = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            manager.killBackgroundProcesses(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}