package com.junlong0716.base.module.http.download

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *@author: EdsionLi
 *@description:
 *@date: Created in 2018/8/3 下午12:48
 *@modified by:
 */
interface DownloadService {
    @Streaming
    @GET
    fun startDownload(@Url downloadFileUrl: String): Flowable<ResponseBody>
}