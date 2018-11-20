package com.junlong0716.base.module.http

import android.content.Context
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonParseException
import com.junlong0716.base.module.R
import com.junlong0716.base.module.constant.Constant.ACCOUNT_LOG_OUT
import com.junlong0716.base.module.http.base.BasicResponse
import com.junlong0716.base.module.rx.bus.RxBus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.adapter.rxjava2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException


/**
 * @author: 巴黎没有摩天轮Li
 * @description: 处理返回体
 * @date: Created in 下午12:55 2017/12/29
 * @modified by:
 */
abstract class DefaultObserver<T : BasicResponse<*>>(context: Context) : Observer<T> {
    private var mContext = context

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(t: T) {
        if (t.code == 0) {
            onSuccess(t)
        } else {
            if (t.code == RetrofitClient.instance.getLogOutCode()) {
                ToastUtils.showShort("登录过期！请重新登录！")
                RxBus.default.post("",ACCOUNT_LOG_OUT)
            }
            onFail(t)
        }
    }

    /**
     *网络请求失败
     * @param response 服务器返回的数据
     */
    abstract fun onFail(response: T)

    /**
     *请求数据失败，指在请求网络API接口请求方式时，出现无法联网、缺少权限，内存泄露等原因导致无法连接到请求数据源。
     * @param response 服务器返回的数据
     */
    abstract fun onError(msg: String)

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract fun onSuccess(response: T)


    override fun onError(e: Throwable) {
        LogUtils.e("Retrofit", e.message)
        if (e is HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK)
        } else if (e is ConnectException || e is UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR)
        } else if (e is InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT)
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR)
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR)
        }
    }


    override fun onComplete() {

    }


    /**
     * 请求异常
     *
     * @param reason
     */
    private fun onException(reason: ExceptionReason) {
        when (reason) {
            DefaultObserver.ExceptionReason.CONNECT_ERROR -> {
                ToastUtils.showShort(R.string.common_connect_error, Toast.LENGTH_SHORT)
                onError(mContext.getString(R.string.common_connect_error))
            }


            DefaultObserver.ExceptionReason.CONNECT_TIMEOUT -> {
                ToastUtils.showShort(R.string.common_connect_timeout, Toast.LENGTH_SHORT)
                onError(mContext.getString(R.string.common_connect_timeout))
            }

            DefaultObserver.ExceptionReason.BAD_NETWORK -> {
                ToastUtils.showShort(R.string.common_bad_network, Toast.LENGTH_SHORT)
                onError(mContext.getString(R.string.common_bad_network))
            }

            DefaultObserver.ExceptionReason.PARSE_ERROR -> {
                ToastUtils.showShort(R.string.common_parse_error, Toast.LENGTH_SHORT)
                onError(mContext.getString(R.string.common_parse_error))
            }

            DefaultObserver.ExceptionReason.UNKNOWN_ERROR -> {
                ToastUtils.showShort(R.string.common_unknown_error, Toast.LENGTH_SHORT)
                onError(mContext.getString(R.string.common_unknown_error))
            }

            else -> {
                ToastUtils.showShort(R.string.common_unknown_error)
                onError(mContext.getString(R.string.common_unknown_error))
            }
        }
    }

    /**
     * 请求网络失败原因
     */
    enum class ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR
    }
}
