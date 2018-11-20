package com.junlong0716.base.module.rx

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/7/11.
 * 线程调度
 */
object RxSchedulers {
    fun <T> io_main_observable(activity: RxAppCompatActivity): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(activity.bindToLifecycle())
        }
    }

    fun <T> io_main_flowable(activity: RxAppCompatActivity): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(activity.bindToLifecycle())
        }
    }

    fun <T> io_main_observable(fragment: RxFragment): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(fragment.bindToLifecycle())
        }
    }

    fun <T> io_main_flowable(fragment: RxFragment): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(fragment.bindToLifecycle())
        }
    }
}