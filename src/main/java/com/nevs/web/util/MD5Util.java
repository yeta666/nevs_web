package com.nevs.web.util;

import org.springframework.util.DigestUtils;

/**
 * MD5工具类
 * @author YETA
 * @date 2018/05/18/15:12
 */
public class MD5Util {
    
    private static final String SLAT = "aslkdjf0923irlksadmnf092!2r2093#$^$^%&";

    public static String getMd5(String message) {
        String base = message + "/" + SLAT;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
