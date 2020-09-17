package com.leo.cart.interceptor;

import com.leo.cart.entity.Member;
import com.leo.cart.entity.UserInfo;
import com.leo.common.constant.CartConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 设计一个拦截器，
 * @author Liu
 */
//@Component
public class CartInterceptor implements HandlerInterceptor {

    /**
     * 使用threadlocal对用户个人信息进行保存，在一次请求的同一个线程内都可以获取用户的相关信息
     */
    public static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal();

    /**
     * 在目标方法执行之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取session中的信息，检测用户是否登录
        HttpSession session = request.getSession();
        Member member = (Member)session.getAttribute("mm");
        UserInfo userInfo = new UserInfo();
        if (member != null){
            //用户登录
            userInfo.setId(member.getId().toString());
        }
        //检测用户是否含有临时用户的key
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(CartConstant.USER_KEY)){
                    userInfo.setTemp(cookie.getValue());
                }
            }
        }
        if (StringUtils.isEmpty(userInfo.getTemp())){
            String tempId = UUID.randomUUID().toString();
            userInfo.setTemp(tempId);
        }
        THREAD_LOCAL.set(userInfo);
        //放行操作
        return true;
    }

    /**
     * 在操作之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfo userInfo = THREAD_LOCAL.get();
        if (userInfo.getTemp() == null){
            Cookie cookie = new Cookie(CartConstant.USER_KEY, userInfo.getTemp());
            cookie.setDomain("leo.com");
            response.addCookie(cookie);
        }
    }
}
