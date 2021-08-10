package com.cy.pj.common.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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
    public SecurityManager securityManager(Realm realm, CacheManager cacheManager,
                                           RememberMeManager rememberManager) {
        DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
        sManager.setRealm(realm);
        sManager.setCacheManager(cacheManager);
        sManager.setRememberMeManager(rememberManager);
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
        //静态资源允许匿名访问:"anon"(shiro框架指定这些常量值)//官网https://shiro.apache.org/web.html
        map.put("/bower_components/**", DefaultFilter.anon.toString());
//        map.put("/bower_components/**","anon");
        map.put("/build/**", DefaultFilter.anon.toString());
        map.put("/dist/**", DefaultFilter.anon.toString());
        map.put("/plugins/**", DefaultFilter.anon.toString());
        map.put("/user/doLogin", DefaultFilter.anon.toString());
        map.put("/doLogout", DefaultFilter.logout.toString());//触发此url时系统底层会清除session,然后跳转到LoginUrl
        //除了匿名访问的资源,其它都要认证("authc")后访问,要记住,需认证访问的资源要写在下面
//        map.put("/**", DefaultFilter.authc.toString());
        map.put("/**", DefaultFilter.user.toString());//记住我功能实现时,认证规则需要修改,user表示可以通过cookie在浏览器
        //拿到的信息去验证
        sfBean.setFilterChainDefinitionMap(map);
        return sfBean;
    }

    /**
     * 第一步:配置bean对象的生命周期管理(SpringBoot可以不配置)。
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 第二步: 通过如下配置要为目标业务对象创建代理对象（SpringBoot中可省略）。
     */
    @DependsOn("lifecycleBeanPostProcessor")
    @Bean
    public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    /**
     * 第三步:配置advisor对象,shiro框架底层会通过此对象的matchs方法返回
     * 值(类似切入点)决定是否创建代理对象,进行权限控制。
     * shiro授权配置
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 配置shiro中的缓存管理器,通过此缓存管理器对象中的韩村对象来对授权信息进行缓存
     * 说明:
     * 1)方法名不能写cacheManage(可能会与spring中的cacheManage对象有重复)
     * 2)方法的返回值对象要注入给SecurityManage对象
     * <p>
     * 当我们访问一个授权方法时,shiro框架会调用SecurityManage对象进行对用户进行权限检测
     * 此时需要获取用户权限信息,如何获得这个权限信息?假如没有配置缓存,每次都会查询数据库.
     * 配置缓存以后securityManage对象会优先从shiro缓存查询数据,缓存中没有才会查询数据库
     * <p>
     * 注意:当修改了用户权限,有了缓存以后,此权限应该在下一次登录以后有效
     */
    @Bean
    public CacheManager shiroCacheManager() {//CacheManage不是cache,而是cache管理器
        return new MemoryConstrainedCacheManager();
    }

    /**
     * 配置记住我管理器对象,shiro框架中的记住我,第层要给予cookie对象实现
     * 通过ResponseBody对象写到客户端
     * 创建一个cookie对象存放在客户端实现记住我功能
     */
    @Bean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager cManager =
                new CookieRememberMeManager();
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(10 * 60);
        cManager.setCookie(cookie);
        return cManager;
    }

    /**
     *
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sManager =
                new DefaultWebSessionManager();
        sManager.setGlobalSessionTimeout(60 * 60 * 1000);
        return sManager;
    }


}
