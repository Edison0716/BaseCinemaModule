package com.junlong0716.base.module.http.base

import okhttp3.ResponseBody

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/3/15 下午5:15
 *@modified by:
 */
interface RequestJsonCallback {
    /**
     * 数据请求成功
     * @param data 请求到的数据
     */
    fun onSuccess(body: ResponseBody)

    /**
     * 使用网络API接口请求方式时，虽然已经请求成功但是由
     * 于`msg`的原因无法正常返回数据。
     */
    fun onFailure(e: Throwable)

    /**
     * 请求数据失败，指在请求网络API接口请求方式时，出现无法联网、
     * 缺少权限，内存泄露等原因导致无法连接到请求数据源。
     */
    fun onError(msg:String)
}