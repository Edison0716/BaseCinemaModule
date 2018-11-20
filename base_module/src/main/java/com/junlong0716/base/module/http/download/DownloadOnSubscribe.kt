package com.junlong0716.base.module.http.download

import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import okhttp3.ResponseBody
import okio.*
import java.io.File
import java.io.IOException

/**
 *@author: EdsionLi
 *@description: 自定义下载 发射器
 *@date: Created in 2018/8/3 上午11:45
 *@modified by:
 */
class DownloadOnSubscribe @Throws(IOException::class) constructor(responseBody: ResponseBody, savePath: String, fileName: String) : FlowableOnSubscribe<Any> {
    private var mFlowableEmitter: FlowableEmitter<Any>? = null
    //默认保存地址
    private var mSavePath: String = savePath
    //文件名
    private var mFileName: String = fileName
    //已上传
    private var mUploaded = 0L
    //总长度
    private var mSumLength = 0L
    private var mPercent = 0

    private var mSource: Source? = null
    private var mProgressSource: Source? = null
    private var mSink: BufferedSink? = null


    init {
        mSumLength = responseBody.contentLength()

        mSource = responseBody.source()

        mProgressSource = getProgressSource(mSource!!)

        mSink = Okio.buffer(Okio.sink(File(mSavePath + mFileName)))
    }

    override fun subscribe(emitter: FlowableEmitter<Any>) {
        this.mFlowableEmitter = emitter
        try {
            mSink!!.writeAll(Okio.buffer(mProgressSource!!))
            mSink!!.close()
            mFlowableEmitter!!.onNext(mSavePath + mFileName)
            mFlowableEmitter!!.onComplete()
        } catch (exception: Exception) {
            if (!mFlowableEmitter!!.isCancelled){
                mFlowableEmitter!!.onError(exception)
            }
        }
    }

    private fun getProgressSource(source: Source): ForwardingSource {
        return object : ForwardingSource(source) {
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val read = super.read(sink, byteCount)
                onRead(read)
                return read
            }
        }
    }

    private fun onRead(read: Long) {
        mUploaded += if (read == -1L) 0 else read
        if (mSumLength <= 0) {
            onProgress(-1)
        } else {
            onProgress((100 * mUploaded / mSumLength).toInt())
        }
    }

    private fun onProgress(percent: Int) {
        var percent = percent
        if (mFlowableEmitter == null) return
        if (percent == mPercent) return
        mPercent = percent
        if (percent >= 100) {
            percent = 100
            mFlowableEmitter!!.onNext(percent)
            return
        }
        mFlowableEmitter!!.onNext(percent)
    }

}