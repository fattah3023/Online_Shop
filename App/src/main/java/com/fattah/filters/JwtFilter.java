package com.fattah.filters;

import com.fattah.service.user.UserService;
import com.fattah.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    public static final String CURRENT_USER = "CURRENT_USER";
    private final JwtUtil util;
    private final UserService service;

    @Autowired
    public JwtFilter(JwtUtil util, UserService service) {
        this.util = util;
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String header = httpRequest.getHeader("Authorization");
        String prefix = "Bearer ";
        String token = "";
        if (header != null && header.startsWith(prefix)) {
            token = header.substring(prefix.length());
        }
        if (!token.isEmpty() && util.validateToken(token)) {
            String username = util.getUsername(token);
            httpRequest.setAttribute(CURRENT_USER, service.readUserByUsername(username));
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.getWriter().write("Access Denied");
        }

    }
}
