package com.cy.pj.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 在此controller定义页面请求的响应
 */
@Controller
@RequestMapping("/")
public class PageController {


    /**
     * 跳转到登录页面
     */
    @RequestMapping("doLoginUI")
    public String doLoginUI() {
        return "login";
    }

    /**
     * 返回分页页面
     */
    @RequestMapping("doPageUI")
    public String doPageUI() {
       /* try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }*/
        return "common/page";
    }

    @RequestMapping("doIndexUI")
    public String doIndexUI() {
        return "starter";//返回给DispatcherServlet
    }//DispatcherServlet会调用视图解析器对象view进行解析,并将解析结果响应客户端.

    /**
     * 基于此方法返回日志列表页面
     */
   /* @RequestMapping("log/log_list")
    public String doLogUI() {
        return "sys/log_list";
    }*/

    /**
     * 返回菜单页面分页页面
     */
    /*@RequestMapping("menu/menu_list")
    public String doMenuUI() {
        return "sys/menu_list";
    }*/

    /**
     * 返回菜单页面分页页面
     */
    @RequestMapping("{module}/{moduleUI}")
    public String doModuleUI(@PathVariable String moduleUI) {
        return "sys/" + moduleUI;
    }

}





