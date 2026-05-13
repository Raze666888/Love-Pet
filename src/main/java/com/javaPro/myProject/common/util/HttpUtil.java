package com.javaPro.myProject.common.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class HttpUtil {

    public static ServletRequestAttributes getRequestAttributes()
    {
        try
        {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static HttpServletRequest getRequest(){
        try
        {
            return Objects.requireNonNull(getRequestAttributes()).getRequest();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static HttpServletResponse getResponse()
    {
        try
        {
            return getRequestAttributes().getResponse();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static String getRequestAttr(String s){
        try
        {
            return (String) Objects.requireNonNull(getRequestAttributes()).getRequest().getAttribute(s);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static String getRequestPara(String s){
        try
        {
            return Objects.requireNonNull(getRequestAttributes()).getRequest().getParameter(s);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static String getSession(String s){
        try
        {
            return (String) Objects.requireNonNull(getRequestAttributes()).getRequest().getSession().getAttribute(s);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static String getUser(){

        return (String) Objects.requireNonNull(getRequestAttributes()).getRequest().getHeader("userId");
    }
}
