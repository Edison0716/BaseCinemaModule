package com.junlong0716.base.module.http.download

import android.content.Context
import com.blankj.utilcode.util.NetworkUtils
import com.junlong0716.base.module.R
import com.junlong0716.base.module.http.exception.ServerException
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/8/3 下午1:11
 *@modified by:
 */
abstract class DownloadSubscriber(context: Context) : Subscriber<Any> {
    private lateinit var mSubscription: Subscription
    private var mContext = context

    override fun onComplete() {

    }

    override fun onSubscribe(s: Subscription?) {
        mSubscription = s!!
        mSubscription.request(1)
    }

    override fun onNext(t: Any?) {
        if (t is Int) {
            onProgress(t)
        }

        if (t is String) {
            onNext(t)
        }

        mSubscription.request(1)
    }

    override fun onError(e: Throwable?) {
        if (!NetworkUtils.isAvailableByPing()) {
            onError(ServerException.ERROR_NETWORK, mContext.resources.getString(R.string.common_connect_error))
        } else if (e is ServerException) {
            onError(e.getErrorCode(), e.message)
        } else {
            onError(ServerException.ERROR_OTHER, e!!.message)
        }
    }

    abstract fun onNext(result: String)

    abstract fun onProgress(percent: Int?)

    abstract fun onError(errorCode: Int, msg: String?)
}