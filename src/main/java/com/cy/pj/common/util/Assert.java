package com.cy.pj.common.util;

import com.cy.pj.common.exception.ServiceException;

public class Assert {


    /**
     * 对象是否为空
     */
    public static void isNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 字符串是否为空
     */
    public static void isEmpty(String content, String message) {
        if (content == null || "".equals(content)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 参数有效性校验
     */
    public static void isArgumentValid(boolean statement, String message) {
        if (statement) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 业务校验
     */
    public static void isServiceValid(boolean statement, String message) {
        if (statement) {
            throw new ServiceException(message);
        }
    }
}
