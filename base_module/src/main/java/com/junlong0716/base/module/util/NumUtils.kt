package com.junlong0716.base.module.util

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * @author: EdsionLi
 * @description: 去除double 后面多余
 * @date: Created in 2018/4/12 下午12:32
 * @modified by:
 */

object NumUtils {

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    fun subZeroAndDot(s: String): String {
        var s = s
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "")//去掉多余的0
            s = s.replace("[.]$".toRegex(), "")//如最后一位是.则去掉
        }
        return s
    }
}
