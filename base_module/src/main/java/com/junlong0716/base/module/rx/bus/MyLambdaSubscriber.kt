package com.junlong0716.base.module.rx.bus

import org.reactivestreams.Subscription

import java.util.concurrent.atomic.AtomicReference

import io.reactivex.FlowableSubscriber
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.observers.LambdaConsumerIntrospection
import io.reactivex.plugins.RxJavaPlugins

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2018/05/08
 * desc  :
</pre> *
 */
class MyLambdaSubscriber<T>(private val onNext: Consumer<in T>, private val onError: Consumer<in Throwable>,
                            private val onComplete: Action,
                            private val onSubscribe: Consumer<in Subscription>) : AtomicReference<Subscription>(), FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection {

    override fun onSubscribe(s: Subscription) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                s.cancel()
                onError(ex)
            }
        }
    }

    override fun onNext(t: T) {
        if (!isDisposed) {
            try {
                onNext.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                onError(e)
            }

        }
    }

    override fun onError(t: Throwable) {
        if (get() !== SubscriptionHelper.CANCELLED) {
            try {
                onError.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }

        } else {
            RxJavaPlugins.onError(t)
        }
    }

    override fun onComplete() {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                onComplete.run()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }

        }
    }

    override fun dispose() {
        cancel()
    }

    override fun isDisposed(): Boolean {
        return get() === SubscriptionHelper.CANCELLED
    }

    override fun request(n: Long) {
        get().request(n)
    }

    override fun cancel() {
        SubscriptionHelper.cancel(this)
    }

    override fun hasCustomOnError(): Boolean {
        return onError !== Functions.ON_ERROR_MISSING
    }

    companion object {
        private const val serialVersionUID = -7251123623727029452L
    }
}
