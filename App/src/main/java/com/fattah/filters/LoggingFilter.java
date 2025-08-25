package com.fattah.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;
        System.out.println(httpServletRequest.getRequestURL()+" "+"["+httpServletRequest.getMethod()+"]");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
