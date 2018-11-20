package com.junlong0716.base.module.manager

import android.content.Context
import android.text.TextUtils
import com.junlong0716.base.module.Constant
import com.junlong0716.base.module.entity.UserAccountEntity
import com.junlong0716.base.module.entity.UserInfoEntity
import com.junlong0716.base.module.util.JSONUtil
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 *@author: EdsionLi
 *@description: 共享属性管理器
 *@date: Created in 2018/3/16 上午11:28
 *@modified by:
 */
object SPTokenManager {

    fun getUserAccount(context: Context): UserAccountEntity? {
        val aContext = context.createPackageContext("com.fbi.rainbowpavilion",
                RxAppCompatActivity.CONTEXT_IGNORE_SECURITY)
        val sp = aContext.getSharedPreferences("spUtils",
                RxAppCompatActivity.MODE_PRIVATE)
        val spUserAccount = sp.getString(Constant.USER_ACCOUNT_JSON, "")
        return if (!TextUtils.isEmpty(spUserAccount)) {
            JSONUtil.fromJson(spUserAccount!!, UserAccountEntity::class.java)
        } else {
            null
        }
    }


    fun getUserInfo(context: Context): UserInfoEntity? {
        //通过共享SP获取Token
        val aContext = context.createPackageContext("com.fbi.rainbowpavilion",
                RxAppCompatActivity.CONTEXT_IGNORE_SECURITY)
        val sp = aContext.getSharedPreferences("spUtils",
                RxAppCompatActivity.MODE_PRIVATE)
        val spUserInfo = sp.getString(Constant.USER_INFO_JSON, "")
        return if (!TextUtils.isEmpty(spUserInfo)) {
            JSONUtil.fromJson(spUserInfo!!, UserInfoEntity::class.java)
        } else {
            null
        }
    }
}