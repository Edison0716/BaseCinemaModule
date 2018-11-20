package com.junlong0716.base.module.http.exception

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/9/19.
 */

class ServerException(message: String, errorCode: Int) : Exception(message) {

    var mErrorCode = ERROR_OTHER

    init {
        mErrorCode = errorCode
    }

    companion object {
        val ERROR_NETWORK = -1
        val ERROR_OTHER = -2
    }

    fun getErrorCode(): Int {
        return mErrorCode
    }
}
