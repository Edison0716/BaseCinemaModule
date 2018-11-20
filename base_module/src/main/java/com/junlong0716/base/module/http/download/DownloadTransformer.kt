package com.junlong0716.base.module.http.download

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.reactivestreams.Publisher

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/8/3 上午11:42
 *@modified by:
 */
class DownloadTransformer(path: String, fileName: String) : FlowableTransformer<ResponseBody, Any> {
    private var mPath = path
    private var mFileName = fileName
    override fun apply(upstream: Flowable<ResponseBody>): Publisher<Any> {
        return upstream.flatMap { responseBody ->
            val downLoadOnSubscribe = DownloadOnSubscribe(responseBody, mPath, mFileName)
            Flowable
                    .create(downLoadOnSubscribe, BackpressureStrategy.BUFFER)
        }
    }
}