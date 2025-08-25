package com.fattah.config;
import com.fattah.filters.JwtFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class
 FilterConfig {
    private final JwtFilter util;

      @Autowired
    public FilterConfig(JwtFilter util) {
          this.util = util;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean<Filter> filter(){
        FilterRegistrationBean<Filter> bean=new FilterRegistrationBean<>();
        bean.setOrder(1);
        bean.setFilter(util);
        bean.addUrlPatterns("/api/panel/*");
        bean.setName("jwtFilter");
        return bean;
    }


}
