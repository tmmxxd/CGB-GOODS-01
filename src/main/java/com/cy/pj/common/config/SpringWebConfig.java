package com.cy.pj.common.config;

import com.cy.pj.common.web.TimeAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 此类中只做和web相关的配置
 */
@Configuration
public class SpringWebConfig implements WebMvcConfigurer {

    /**
     * 注册拦截器并设置其拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置要拦截的路径
        registry.addInterceptor(new TimeAccessInterceptor()).addPathPatterns("/user/doLogin");
    }
}
