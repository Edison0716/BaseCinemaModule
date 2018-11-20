package com.junlong0716.base.module.util;

import android.util.Log;

import com.junlong0716.base.module.rx.bus.TagMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

/**
 * @author: EdsionLi
 * @description: 粘性事件 缓存工具类 从缓存中查找保存的粘性事件
 * @date: Created in 2018/8/6 下午4:06
 * @modified by:
 */
public final class CacheUtils {
    private final Map<Class, List<TagMessage>> stickyEventsMap = new ConcurrentHashMap<>();

    private final Map<Object, List<Disposable>> disposablesMap = new ConcurrentHashMap<>();

    private CacheUtils() {
    }

    //单例
    public static CacheUtils getInstance() {
        return Holder.CACHE_UTILS;
    }

    //添加粘性事件
    public void addStickyEvent(final TagMessage stickyEvent) {
        Class<?> eventType = stickyEvent.getEvent().getClass();
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) {
                stickyEvents = new ArrayList<>();
                stickyEvents.add(stickyEvent);
                stickyEventsMap.put(eventType, stickyEvents);
            } else {
                int indexOf = stickyEvents.indexOf(stickyEvent);
                if (indexOf == -1) {// 不存在直接插入
                    stickyEvents.add(stickyEvent);
                } else {// 存在则覆盖
                    stickyEvents.set(indexOf, stickyEvent);
                }
            }
        }
    }

    //从缓存中查找粘性事件
    public TagMessage findStickyEvent(final Class eventType, final String tag) {
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) return null;
            int size = stickyEvents.size();
            TagMessage res = null;
            for (int i = size - 1; i >= 0; --i) {
                TagMessage stickyEvent = stickyEvents.get(i);
                if (stickyEvent.isSameType(eventType, tag)) {
                    res = stickyEvents.get(i);
                    break;
                }
            }
            return res;
        }
    }

    //添加订阅
    public void addDisposable(Object subscriber, Disposable disposable) {
        synchronized (disposablesMap) {
            List<Disposable> list = disposablesMap.get(subscriber);
            if (list == null) {
                list = new ArrayList<>();
                list.add(disposable);
                disposablesMap.put(subscriber, list);
            } else {
                list.add(disposable);
            }
        }
    }

    //移除订阅
    public void removeDisposables(final Object subscriber) {
        synchronized (disposablesMap) {
            List<Disposable> disposables = disposablesMap.get(subscriber);
            if (disposables == null) return;
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            disposables.clear();
            disposablesMap.remove(subscriber);
        }
    }

    //消费事件
    public void removeStickyEvent(final TagMessage stickyEvent) {
        Class<?> eventType = stickyEvent.getEvent().getClass();
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            int indexOf = stickyEvents.indexOf(stickyEvent);
            if (indexOf == -1) {
                // 事件不存在
                Log.e("no sticky event", "粘性事件不存在！");
            } else {
                // 存在则移除事件
                stickyEvents.remove(indexOf);
            }
        }
    }

    //消费事件
    public void removeStickyEvent(final String event) {
        TagMessage tagMessage = new TagMessage(event, "");
        Class<?> eventType = tagMessage.getEvent().getClass();
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            int indexOf = stickyEvents.indexOf(tagMessage);
            if (indexOf == -1) {
                // 事件不存在
                Log.e("no sticky event", "粘性事件不存在！");
            } else {
                // 存在则移除事件
                stickyEvents.remove(indexOf);
            }
        }
    }

    //消费事件
    public void removeStickyEvent(final String event, final String tag) {
        TagMessage tagMessage = new TagMessage(event, tag);
        Class<?> eventType = tagMessage.getEvent().getClass();
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            int indexOf = stickyEvents.indexOf(tagMessage);
            if (indexOf == -1) {
                // 事件不存在
                Log.e("no sticky event", "粘性事件不存在！");
            } else {
                // 存在则移除事件
                stickyEvents.remove(indexOf);
            }
        }
    }

    //单例Holder
    private static class Holder {
        private static final CacheUtils CACHE_UTILS = new CacheUtils();
    }
}
