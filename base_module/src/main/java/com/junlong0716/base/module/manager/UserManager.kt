package com.junlong0716.base.module.manager

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.junlong0716.base.module.Constant
import com.junlong0716.base.module.entity.UserAccountEntity
import com.junlong0716.base.module.entity.UserInfoEntity
import com.junlong0716.base.module.util.JSONUtil

/**
 *@author: 巴黎没有摩天轮Li
 *@description:账号管理类
 *@date: Created in 下午9:54 2018/3/15
 *@modified by:
 */
object UserManager {
    //保存用户登录信息
    fun putUserAccount(userAccount: UserAccountEntity) {
        val userAccountJson = JSONUtil.toJSON(userAccount)
        SPUtils.getInstance().put(Constant.USER_ACCOUNT_JSON, userAccountJson)
    }

    //获取用户登录信息
    fun getUserAccount(): UserAccountEntity? {
        val userAccount = SPUtils.getInstance().getString(Constant.USER_ACCOUNT_JSON, "")
        return if (!TextUtils.isEmpty(userAccount)) {
            JSONUtil.fromJson(userAccount!!, UserAccountEntity::class.java)
        } else null
    }

    //保存用户信息
    fun putUserInfo(userInfo: UserInfoEntity) {
        val userInfoJson = JSONUtil.toJSON(userInfo)
        SPUtils.getInstance().put(Constant.USER_INFO_JSON, userInfoJson)
    }

    //获取用户信息
    fun getUserInfo(): UserInfoEntity? {
        val userInfo = SPUtils.getInstance().getString(Constant.USER_INFO_JSON, "")
        return if (!TextUtils.isEmpty(userInfo)) {
            JSONUtil.fromJson(userInfo!!, UserInfoEntity::class.java)
        } else null
    }


    //保存登录状态
    fun setLoginState(loginState: Boolean) {
        SPUtils.getInstance().put(Constant.LOGIN_STATE, loginState)
    }

    //获取登录状态
    fun getLoginState(): Boolean = SPUtils.getInstance().getBoolean(Constant.LOGIN_STATE, false)

    //程序是否首次运行
    fun setFirstOpenApp(isFirst: Boolean) {
        SPUtils.getInstance().put(Constant.IS_FIRST, isFirst)
    }

    fun isFirstOpenApp(): Boolean {
        return SPUtils.getInstance().getBoolean(Constant.IS_FIRST,true)
    }
}