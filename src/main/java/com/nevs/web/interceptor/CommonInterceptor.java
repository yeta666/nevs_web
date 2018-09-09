package com.nevs.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.nevs.web.util.CommonResponse;
import com.nevs.web.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截器类
 * 拦截器可以拿到原始的HTTP请求和响应的信息，也可以拿到方法的信息，但是拿不到参数
 * Created by YETA666 on 2018/4/22 0022.
 */
@Component
public class CommonInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(CommonInterceptor.class);

    /**
     * 判断请求uri是在允许范围内
     * @param uri
     * @return
     */
    public boolean isPremited(String uri) {
        String[] permitUris = {"/user/insert", "/login", "/logout", "/upload", "/download", "/vCode"};        //注册、登陆、注销、上传、获取图片、下载、获取验证码
        for (String permit : permitUris) {
            if (uri.indexOf(permit) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在请求处理之前调用
     * @param request
     * @param response
     * @param handler
     * @return  true表示通过拦截，返回请求目标
     *          false表示没有通过拦截，返回空白页面
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOG.info("拦截器preHandle()");
        //获取session
        HttpSession httpSession = request.getSession();
        //获取application
        ServletContext servletContext = httpSession.getServletContext();
        //获取在线用户列表
        List<String> onlines = (List<String>) servletContext.getAttribute("onlines");

        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        //获取输出流
        PrintWriter out = null;

        //获取请求uri
        String uri = request.getServletPath();

        //判断是否访问的是允许的资源
        if (isPremited(uri)) {
            LOG.info("请求访问[ " + uri + " ]，允许的uri，放行");
            return true;
        }

        //获取参数中的token
        String requestToken = request.getParameter("token");
        if (requestToken == null || "".equals(requestToken)) {
            out = response.getWriter();
            out.append(JSONObject.toJSONString(new CommonResponse(false, 2, "请求token为空")));
            out.close();
            LOG.info("请求访问[ " + uri + " ]，请求token为空，拦截");
            return false;
        }

        //判断该用户名是否已经存在于在线用户列表
        for (String online: onlines) {
            if (MD5Util.getMd5(online).equals(requestToken)) {
                LOG.info("请求访问[ " + uri + " ]，验证token通过，放行");
                return true;
            }
        }
        //如果不存在
        out = response.getWriter();
        out.append(JSONObject.toJSONString(new CommonResponse(false, 2, "token超时或未登录")));
        out.close();
        LOG.info("请求访问[ " + uri + " ]，token超时或未登录，拦截");
        return false;
    }

    /**
     * 在请求处理之后调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //LOG.info("拦截器postHandle()");
    }

    /**
     * 在请求结束之后调用
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //LOG.info("拦截器afterCompletion()");
    }
}
