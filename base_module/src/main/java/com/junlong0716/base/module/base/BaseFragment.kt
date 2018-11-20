package com.junlong0716.base.module.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 *@author: 巴黎没有摩天轮Li
 *@description: BaseFragment 懒加载 基类
 *@date: Created in 上午09:48 2018/1/2
 *@modified by:
 */
abstract class BaseFragment<P : IPresenter> : RxFragment() {
    protected val TAG = this.javaClass.simpleName
    private var isViewPrepared: Boolean = false // 标识fragment视图已经初始化完毕
    private var hasFetchData: Boolean = false // 标识已经触发过懒加载数据
    var mPresenter: P? = null

    private var mRootView: View? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initViews(mRootView: View?)

    protected abstract fun lazyFetchData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachPresenter()
    }

    abstract fun attachPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mRootView = inflater.inflate(getLayoutId(), container, false)
        initViews(mRootView)
        return mRootView
    }

    private fun lazyFetchDataIfPrepared() {
        if (userVisibleHint && !hasFetchData && isViewPrepared) {
            hasFetchData = true
            lazyFetchData()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepared = true
        lazyFetchDataIfPrepared()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasFetchData = false
        isViewPrepared = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter!!.onDestroy()
        isViewPrepared = false
        hasFetchData = false
        mPresenter = null
        mRootView = null
    }

    protected fun go(tarActivity: Class<out AppCompatActivity>) {
        val intent = Intent(activity, tarActivity)
        startActivity(intent)
    }

    protected fun go(tarActivity: Class<out AppCompatActivity>, bundle: Bundle) {
        val intent = Intent(activity, tarActivity)
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