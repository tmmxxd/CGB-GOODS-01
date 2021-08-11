package com.cy.pj.common.web;

import com.cy.pj.common.exception.ServiceException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * spring MVC中的拦截器定义:基于此对象实现对登录操作的事件访问限制
 * 底层调用过程
 * DispatcherServlet --> HandlerInterceptor -->SysUerController(Handler)
 */
public class TimeAccessInterceptor implements HandlerInterceptor {

    /**
     * 此方法会在后端控制方法执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("==============preHandle===========");
        //1.定义允许访问的时间范围
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long start = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 16);
        long end = c.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        //2.获取当前事件进行业务判定
        if (currentTime < start || currentTime > end) {
            throw new ServiceException("请在9-14点规定时间内登录");
        }

        return true;//false表示拒绝对handler(controller)执行,true表示放行
    }

}
