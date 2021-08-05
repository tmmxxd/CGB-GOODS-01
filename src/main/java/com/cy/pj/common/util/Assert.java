package com.cy.pj.common.util;

import com.cy.pj.common.exception.ServiceException;

public class Assert {

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
