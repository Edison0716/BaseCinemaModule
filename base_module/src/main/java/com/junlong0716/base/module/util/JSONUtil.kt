package com.junlong0716.base.module.util

import com.google.gson.GsonBuilder

/**
 *
 * @author EdsionLi
 * @date 2017/4/16
 */

object JSONUtil {

    private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()


    fun <T> fromJson(json: String, clz: Class<T>): T = gson.fromJson(json, clz)


    fun toJSON(`object`: Any): String = gson.toJson(`object`)
}
