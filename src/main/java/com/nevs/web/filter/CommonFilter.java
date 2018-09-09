package com.nevs.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器类
 * 过滤器可以拿到原始的HTTP请求和响应的信息，但是拿不到方法的信息
 * @author YETA
 * @date 2018/05/24/20:25
 */
@WebFilter(filterName = "CommonFilter", urlPatterns = "/*")       //注解方式配置
public class CommonFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("过滤器init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //LOGGER.info("过滤器doFilter前...");

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        //设置返回头
        res.setContentType("text/html;charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "0");
        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("XDomainRequestAllowed","1");

        filterChain.doFilter(servletRequest, servletResponse);
        //LOGGER.info("过滤器doFilter后...");
    }

    @Override
    public void destroy() {
        //LOGGER.info("过滤器destroy...");
    }
}
