package com.javaPro.myProject.common.config;

import com.javaPro.myProject.common.handle.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Bean
//    登陆拦截器LoginInterceptor
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 根路径重定向到登录页面
        registry.addRedirectViewController("/", "/login");
    }

    @Override
//    拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())//添加拦截器
                .excludePathPatterns("/login","/toLoginPage","/toRegisterPage","uploading","/web/login","/web/register",
                        "/logout",
                        "/toLogin",
                        "/toRegister",
                        "/web/photoTemplate",
                        "/web/userindex",
                        "/web/userproductlist",
                        "/web/userblog",
                        "/web/userblogdetail",
                        "/web/usercontact",
                        "/web/usersingle",
                        "/web/aipet",
                        "/product/**",
                        "/producttype/**",
                        "/company/**",
                        "/static/**",
                        "/uploads/**",
                        "/test/**",
                        "/comment/**",
                        "/webnotice/**",
                        "/aipet/**"
                )//去除对这些接口地址的拦截
                .addPathPatterns("/web/*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射 - 主要的静态资源映射，优先级最高
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600); // 移除resourceChain以避免路径解析问题

        // 产品图片映射 - 映射到static/img目录
        registry.addResourceHandler("/product/**")
                .addResourceLocations("classpath:/static/img/");

        // 上传文件的静态资源映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");

        // 确保Spring Boot默认的静态资源处理不被覆盖
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/", "classpath:/META-INF/resources/")
                .setCachePeriod(3600);
    }
}
