package com.junlong0716.base.module.rx.bus

import android.annotation.SuppressLint

import com.junlong0716.base.module.util.CacheUtils
import com.junlong0716.base.module.util.FlowableUtils
import com.junlong0716.base.module.util.RxBusUtils

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2017/12/14
 * desc  : 1.1
</pre> *
 */
class RxBus private constructor() {

    private val mBus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    private val mOnError = Consumer<Throwable> { throwable -> RxBusUtils.logE(throwable.toString()) }

    fun post(event: Any) {
        post(event, "", false)
    }

    fun post(event: Any, tag: String) {
        post(event, tag, false)
    }

    fun postSticky(event: Any) {
        post(event, "", true)
    }

    fun postSticky(event: Any, tag: String) {
        post(event, tag, true)
    }

    private fun post(event: Any,
                     tag: String,
                     isSticky: Boolean) {
        RxBusUtils.requireNonNull(event, "event is null")
        RxBusUtils.requireNonNull(tag, "tag is null")

        val msgEvent = TagMessage(event, tag)
        if (isSticky) {
            CacheUtils.getInstance().addStickyEvent(msgEvent)
        }
        mBus.onNext(msgEvent)
    }

    fun <T> subscribe(subscriber: Any,
                      callback: Callback<T>) {
        subscribe(subscriber, "", false, null, callback)
    }

    fun <T> subscribe(subscriber: Any,
                      scheduler: Scheduler,
                      callback: Callback<T>) {
        subscribe(subscriber, "", false, scheduler, callback)
    }

    fun <T> subscribe(subscriber: Any,
                      tag: String,
                      callback: Callback<T>) {
        subscribe(subscriber, tag, false, null, callback)
    }

    fun <T> subscribe(subscriber: Any,
                      tag: String,
                      scheduler: Scheduler,
                      callback: Callback<T>) {
        subscribe(subscriber, tag, false, scheduler, callback)
    }

    fun <T> subscribeSticky(subscriber: Any,
                            callback: Callback<T>) {
        subscribe(subscriber, "", true, null, callback)
    }

    fun <T> subscribeSticky(subscriber: Any,
                            scheduler: Scheduler,
                            callback: Callback<T>) {
        subscribe(subscriber, "", true, scheduler, callback)
    }

    fun <T> subscribeSticky(subscriber: Any,
                            tag: String,
                            callback: Callback<T>) {
        subscribe(subscriber, tag, true, null, callback)
    }

    fun <T> subscribeSticky(subscriber: Any,
                            tag: String,
                            scheduler: Scheduler,
                            callback: Callback<T>) {
        subscribe(subscriber, tag, true, scheduler, callback)
    }

    @SuppressLint("CheckResult")
    private fun <T> subscribe(subscriber: Any,
                              tag: String,
                              isSticky: Boolean,
                              scheduler: Scheduler?,
                              callback: Callback<T>) {
        RxBusUtils.requireNonNull(subscriber, "subscriber is null")
        RxBusUtils.requireNonNull(tag, "tag is null")
        RxBusUtils.requireNonNull(callback, "callback is null")

        val eventType = RxBusUtils.getClassFromCallback(callback)

        val onNext = Consumer<T> { t -> callback.onEvent(t) }

        if (isSticky) {
            val stickyEvent = CacheUtils.getInstance().findStickyEvent(eventType, tag)
            if (stickyEvent != null) {
                val stickyFlowable = Flowable.create(FlowableOnSubscribe<T> { emitter -> emitter.onNext(eventType.cast(stickyEvent.event)) }, BackpressureStrategy.LATEST)
                if (scheduler != null) {
                    stickyFlowable.observeOn(scheduler)
                }
                val stickyDisposable = FlowableUtils.subscribe(stickyFlowable, onNext, mOnError)
                CacheUtils.getInstance().addDisposable(subscriber, stickyDisposable)
            } else {
                RxBusUtils.logW("sticky event is empty.")
            }
        }
        val disposable = FlowableUtils.subscribe(toFlowable(eventType, tag, scheduler), onNext, mOnError)
        CacheUtils.getInstance().addDisposable(subscriber, disposable)
    }

    private fun <T> toFlowable(eventType: Class<T>,
                               tag: String,
                               scheduler: Scheduler?): Flowable<T> {
        val flowable = mBus.ofType(TagMessage::class.java)
                .filter { tagMessage -> tagMessage.isSameType(eventType, tag) }
                .map { tagMessage -> tagMessage.event }
                .cast(eventType)
        return if (scheduler != null) {
            flowable.observeOn(scheduler)
        } else flowable
    }

    fun unregister(subscriber: Any) {
        CacheUtils.getInstance().removeDisposables(subscriber)
    }

    private object Holder {
        val BUS = RxBus()
    }

    interface Callback<in T> {
        fun onEvent(t: T)
    }

    companion object {
        val default: RxBus
            get() = Holder.BUS
    }
}