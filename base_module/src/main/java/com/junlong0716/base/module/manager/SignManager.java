package com.junlong0716.base.module.manager;

import android.content.Context;

import com.junlong0716.base.module.BuildConfig;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author: 巴黎没有摩天轮Li
 * @description: 签名管理类
 * @date: Created in 下午6:47 2018/3/18
 * @modified by:
 */
public class SignManager {
    //获取加签后的sign
    public static String getSign(Map<String, String> orderMap, long time, Context mContent) {
        String token;
        String pin;

        token = SPTokenManager.INSTANCE.getUserAccount(mContent).getImToken();
        pin = SPTokenManager.INSTANCE.getUserAccount(mContent).getPin();

        StringBuilder builder = new StringBuilder();
        orderMap.put("pin", pin);
        orderMap.put("timeStamp", String.valueOf(time));
        for (Map.Entry<String, String> entry : orderMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append(entry.getValue());
        }
        builder.append(token);
        ////////////Logger.e(builder.toString(),"sign-value");
        return getSHA(builder.toString());
    }

    private static String getSHA(String description) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(description.getBytes(Charset.forName("UTF-8")));
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
