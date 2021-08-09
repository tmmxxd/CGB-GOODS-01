package com.cy.pj.common.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @Configuration 注解描述的类为一个配置对象,
 * 此对象也会交给spring管理
 */

@Configuration//bean
public class SpringShiroConfig {

    /**
     * @return SecurityManager 此对象是shiro框架的核心,由此对象完成认证,授权等功能
     * <p>
     * 设计说明:在设计方法时,方法的返回值类型以及方法参数能用抽象则用抽象(多态的体现)
     * @Bean 注解一般用于描述@Configuration注解修饰的类中的方法.目的是将方法的调用
     * 交给Spring框架,并且由spring框架管理这个方法返回值对象(例如将此对象存储到
     * spring容器,并给定起作用域.存储时默认的key为方法名)
     */
    @Bean("securityManager")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
        return sManager;
    }

    /**
     * 初始化过滤器工厂bean对象(底层要通过此对象创建过滤器工厂,然后通过工厂创建过滤器)
     * 思考:
     * 1)为什么要创建过滤器呢?(通过过滤器对象请求数据进行校验,过滤等操作,例如检查用户是否已认证)
     * 2)过滤器对请求或响应数据进行过滤时要指定规则吗?(哪些请求要过滤,哪些不要过滤)
     * 3)对于要过滤的请求,我们通过谁对数据进行校验(例如可以在SecurityManage中进行认证检测)
     */
    @Bean
    @Autowired
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("securityManager") SecurityManager securityManager) {//如何查找实参
        //1.构建工厂bean对象(FactoryBean规范由spring定义,规范的实现在当前模块由shiro框架,例如ShiroFilterFactoryBean)
        ShiroFilterFactoryBean sfBean = new ShiroFilterFactoryBean();
        //2.为sfBean对象注入SecurityManage对象
        sfBean.setSecurityManager(securityManager);
        //3.设置登录url(没有经过认证的请求,需要跳转到这个路径对象的页面)
        sfBean.setLoginUrl("/doLoginUI");
        //4.设置请求过滤规则?(例如哪些请求要进行认证检测,哪些请求不要)
        //定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //静态资源允许匿名访问:"anon"(shiro框架指定这些常量值)
        map.put("/bower_components/**", "anon");
        map.put("/build/**", "anon");
        map.put("/dist/**", "anon");
        map.put("/plugins/**", "anon");
        //除了匿名访问的资源,其它都要认证("authc")后访问,要记住,需认证访问的资源要写在下面
        map.put("/**", "authc");
        sfBean.setFilterChainDefinitionMap(map);
        return sfBean;
    }

}
