package com.junlong0716.base.module.rx.bus

import com.junlong0716.base.module.util.RxBusUtils

/**
 * @author: EdsionLi
 * @description:
 * @date: Created in 2018/8/6 下午4:07
 * @modified by:
 */
class TagMessage (var event: Any, var tag: String) {

    override fun equals(obj: Any?): Boolean {
        if (obj != null && obj is TagMessage) {
            val tagMessage = obj as TagMessage?
            return RxBusUtils.equals(tagMessage!!.event.javaClass, event.javaClass) && RxBusUtils.equals(tagMessage.tag, tag)
        }
        return false
    }

    fun isSameType(eventType: Class<*>, tag: String): Boolean {
        return RxBusUtils.equals(event.javaClass, eventType) && RxBusUtils.equals(this.tag, tag)
    }

    override fun toString(): String {
        return "event: $event, tag: $tag"
    }

    override fun hashCode(): Int {
        var result = event.hashCode()
        result = 31 * result + tag.hashCode()
        return result
    }
}
