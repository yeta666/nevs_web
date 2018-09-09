package com.nevs.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ServletContext监听器类
 * @author YETA
 * @date 2018/05/25/13:30
 */
@WebListener(value = "ServletContext监听器")
public class CommonServletContextLinstener implements ServletContextListener {

    private static final Logger LOGGGE = LoggerFactory.getLogger(CommonHttpSessionLinstener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGGE.info("监听器contextInitialized...");

        //初始化的时候新建一个List，用来存所有在线用户
        List<String> onlines = new ArrayList<>();
        servletContextEvent.getServletContext().setAttribute("onlines", onlines);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGGE.info("监听器contextDestroyed...");

        //销毁的时候移除
        servletContextEvent.getServletContext().removeAttribute("onlines");
    }
}
