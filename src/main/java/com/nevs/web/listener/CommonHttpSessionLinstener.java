package com.nevs.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Arrays;
import java.util.List;

/**
 * Session监听器类
 * @author YETA
 * @date 2018/05/25/13:30
 */
@WebListener(value = "Session监听器")
public class CommonHttpSessionLinstener implements HttpSessionListener {

    private static final Logger LOGGGE = LoggerFactory.getLogger(CommonHttpSessionLinstener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LOGGGE.info("监听器sessionCreated...{}...", httpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        LOGGGE.info("监听器sessionDestroyed...{}...", httpSessionEvent.getSession().getId());

        HttpSession session = httpSessionEvent.getSession();
        Object sid = session.getAttribute("id");
        if (sid != null) {
            List<String> onlines = (List<String>) session.getServletContext().getAttribute("onlines");
            for (String online : onlines) {
                if (sid.equals(online)) {
                    onlines.remove(online);
                    LOGGGE.info("当前在线人数...{}...", onlines.size());
                    LOGGGE.info(Arrays.toString(onlines.toArray()));
                    break;
                }
            }
        }
    }
}
