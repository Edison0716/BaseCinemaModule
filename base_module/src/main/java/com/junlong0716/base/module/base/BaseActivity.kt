package com.junlong0716.base.module.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.AdaptScreenUtils
import com.junlong0716.base.module.manager.ActivityManager
import com.junlong0716.base.module.rx.bus.RxBus
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 *@author: 巴黎没有摩天轮Li
 *@description: Activity 基类
 *@date: Created in 下午12:40 2017/12/29
 *@modified by:
 */
abstract class BaseActivity<P : IPresenter> : RxAppCompatActivity() {
    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //添加activity 到 管理栈中
        ActivityManager.addActivity(this)

        //创建视图前的操作
        beforeSetLayout()

        //创建视图
        setContentView(getLayoutId())

        //绑定Presenter
        attachPresenter()

        //注册RxBus
        registerRxBus()

        //初始化控件
        initView(savedInstanceState)
    }

    abstract fun beforeSetLayout()

    abstract fun attachPresenter()

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun registerRxBus()

    override fun onDestroy() {
        super.onDestroy()
        //解除Presenter 与 Activity 绑定
        this.mPresenter!!.onDestroy()
        this.mPresenter = null

        //解除注册RxBus
        RxBus.default.unregister(this)

        AdaptScreenUtils.closeAdapt(super.getResources())
    }


    protected fun go(tarActivity: Class<out AppCompatActivity>) {
        val intent = Intent(this, tarActivity)
        startActivity(intent)
    }

    protected fun go(tarActivity: Class<out AppCompatActivity>, bundle: Bundle) {
        val intent = Intent(this, tarActivity)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    protected fun go(tarActivityPackage: String, tarActivityPackageName: String) {
        val intent = Intent()
        intent.setClassName(tarActivityPackage, tarActivityPackageName)
        startActivity(intent)
    }

    protected fun go(tarActivityPackage: String, tarActivityPackageName: String, bundle: Bundle) {
        val intent = Intent()
        intent.setClassName(tarActivityPackage, tarActivityPackageName)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}