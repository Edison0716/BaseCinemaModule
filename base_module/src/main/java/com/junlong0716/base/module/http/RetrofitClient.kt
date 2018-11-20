package com.junlong0716.base.module.http

import android.content.Context
import com.blankj.utilcode.util.NetworkUtils
import com.junlong0716.base.module.Constant
import com.junlong0716.base.module.http.download.DownloadService
import com.junlong0716.base.module.http.download.DownloadTransformer
import com.junlong0716.base.module.http.interceptor.CacheInterceptor
import com.junlong0716.base.module.manager.SPTokenManager
import com.junlong0716.base.module.util.FileUtils
import com.junlong0716.base.module.util.FormatJsonUtil
import com.junlong0716.base.module.util.LoggerUtil
import io.reactivex.Flowable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *@author: EdsionLi
 *@description: Retrofit 核心类
 *@date: Created in 2018/8/2 下午3:46
 *@modified by:
 */
class RetrofitClient private constructor() {
    companion object {
        //单例
        @Volatile
        private var INSTANCE: RetrofitClient? = null

        val instance: RetrofitClient
            get() {
                if (INSTANCE == null) {
                    synchronized(RetrofitClient::class.java) {
                        if (INSTANCE == null) {
                            INSTANCE = RetrofitClient()
                        }
                    }
                }
                return INSTANCE!!
            }
    }

    private var mBaseUrl = ""
    private var mRetrofit: Retrofit? = null
    private var mAuthRetrofit: Retrofit? = null
    private var mSavePath: String? = null
    private var mFileName: String? = null
    private var mLogOutCode: Int? = null
    private var mContext: Context? = null


    //设置请求头
    fun setBaseUrl(baseUrl: String): RetrofitClient {
        this.mBaseUrl = baseUrl
        return this
    }

    //账号失效 code
    fun setLogOutCode(code: Int): RetrofitClient {
        this.mLogOutCode = code
        return this
    }

    fun getLogOutCode(): Int? {
        return mLogOutCode
    }

    //初始化
    fun initClient(mContext: Context) {
        this.mContext = mContext
        if (mBaseUrl == "") throw IllegalArgumentException("请在Application中初始化请求头！")
        if (mLogOutCode == null) throw IllegalArgumentException("请在Application中初始化登录过期code！")
        val logInterceptor = HttpLoggingInterceptor(HttpLoggerInterceptor())
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val mOkHttpClient = OkHttpClient
                .Builder()
                .cache(Cache(File(mContext.externalCacheDir, "net_request_cache"), (10 * 1024 * 1024).toLong()))
                .addInterceptor(CacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor())
                .addNetworkInterceptor(logInterceptor)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()

        mRetrofit = Retrofit.Builder()
                .client(mOkHttpClient!!)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val mAuthOkHttpClient = OkHttpClient
                .Builder()
                .cache(Cache(File(mContext.externalCacheDir, "net_request_cache"), (10 * 1024 * 1024).toLong()))
                .addInterceptor(CacheInterceptor())
                .addInterceptor(AuthHttpCacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor())
                .addNetworkInterceptor(logInterceptor)
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()

        mAuthRetrofit = Retrofit.Builder()
                .client(mAuthOkHttpClient!!)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }

    fun getClient(): Retrofit {
        return mRetrofit!!
    }

    fun getAuthClient(): Retrofit {
        return mAuthRetrofit!!
    }

    //认证以后的请求客户端 添加每次都要附带的 pin
    inner class AuthHttpCacheInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain?): Response {
            val original = chain!!.request()
            val originalHttpUrl = original.url()
            val url: HttpUrl


            url = originalHttpUrl.newBuilder()
                    .addQueryParameter("pin", SPTokenManager.getUserAccount(mContext!!)!!.pin)
                    .build()


            val requestBuilder = original.newBuilder()
                    .method(original.method(), original.body()).url(url)
            var request = requestBuilder.build()
            request = if (!NetworkUtils.isConnected()) {
                request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            } else {
                request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
            }
            return chain.proceed(request)
        }
    }

    //日志拦截器
    private inner class HttpLoggerInterceptor : HttpLoggingInterceptor.Logger {
        private val mMessage = StringBuilder()
        override fun log(message: String) {
            var message = message
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mMessage.setLength(0)
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith("]")) {
                message = FormatJsonUtil.formatJson(message)
            }
            mMessage.append(message + "\n")
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                LoggerUtil.d(mMessage.toString())
            }
        }
    }

    //下载文件
    fun downloadFile(url: String): Flowable<Any> {
        return downloadFile(url, null, null)
    }

    //下载文件 指定保存路径
    fun downloadFile(url: String, savePath: String?, fileName: String?): Flowable<Any> {
        mSavePath = savePath
        mFileName = fileName
        if (mSavePath == null || mSavePath!!.trim() == "") mSavePath = FileUtils.getDefaultDownLoadPath()
        if (mFileName == null || mFileName!!.trim() == "") mFileName = FileUtils.getDefaultDownLoadFileName(url)

        //download listener
        val downLoadTransformer = DownloadTransformer(mSavePath!!, mFileName!!)
        return Flowable
                .just(url)
                .flatMap {
                    //重新创建了一个OkHttp客户端 若不创建则收不到下载回调
                    val downloadService = getClient().newBuilder().client(OkHttpClient.Builder().build()).build().create(DownloadService::class.java)
                    return@flatMap downloadService.startDownload(it)
                }
                .compose(downLoadTransformer)
    }
}