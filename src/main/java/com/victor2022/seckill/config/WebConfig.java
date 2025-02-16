package com.victor2022.seckill.config;

import com.victor2022.seckill.filter.SessionExpireFilter;
import com.victor2022.seckill.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/11
 * Time: 20:58
 * Such description:
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    AuthorityInterceptor authorityInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // 映射为 user 的控制器下的所有映射
        //registry.addInterceptor(authorityInterceptor).addPathPatterns("/user/login").excludePathPatterns("/index", "/");
        List<String> urls = new ArrayList<>();
        urls.add("/swagger-resources/**");
        urls.add("/webjars/**");
        urls.add("/v2/**");
        urls.add("/doc.html");
        urls.add("**/swagger-ui.html");
        urls.add("/swagger-ui.html/**");
        registry.addInterceptor(authorityInterceptor).addPathPatterns("/**").excludePathPatterns(urls.toArray(new String[0]));
        super.addInterceptors(registry);
    }

    @Bean("myFilter")
    public Filter uploadFilter() {
        return new SessionExpireFilter();
    }

    @Bean
    public FilterRegistrationBean uploadFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("myFilter"));
        registration.addUrlPatterns("/**");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }


}
