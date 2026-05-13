package com.javaPro.myProject.common.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.common.model.ListByPage;
import com.javaPro.myProject.common.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BaseController {//extends 继承 为了方便子类 去 使用父类的 方法

    /**
     * 对返回给前端的数据进行封装
     * @param rows 结果数
     * @return AjaxResult对象
     */
    public static AjaxResult toAjax(int rows){
        return rows > 0 ? AjaxResult.ok():AjaxResult.error();
    }
    public static AjaxResult toAjax(int rows,String msg ){
        return rows > 0 ? AjaxResult.ok(msg):AjaxResult.error(msg);
    }
    public static AjaxResult toAjax(int rows,List<?> list){
        return rows > 0 ? AjaxResult.ok(list):AjaxResult.error();
    }
    public static AjaxResult toAjax(int rows,String msg , List<?> list){
        return rows > 0 ? AjaxResult.ok(msg,list):AjaxResult.error(msg);
    }

    /**
     * 开启分页
     */
    public static void startPage(){
        HttpServletRequest request = HttpUtil.getRequest();
        HttpServletResponse response = HttpUtil.getResponse();

        // 安全地解析分页参数，提供默认值
        String pageNumStr = request != null ? request.getParameter("pageNum") : null;
        String pageSizeStr = request != null ? request.getParameter("pageSize") : null;

        int pageNum = (pageNumStr != null && !pageNumStr.isEmpty()) ? Integer.parseInt(pageNumStr) : 1;
        int pageSize = (pageSizeStr != null && !pageSizeStr.isEmpty()) ? Integer.parseInt(pageSizeStr) : 10;

        String orderBy = "";
        if (request != null && response != null) {
            orderBy = getOrderBy(request, response, "orderBy");
        }
        if (!"".equals(orderBy)){
            PageHelper.startPage(pageNum,pageSize,orderBy).setReasonable(true);
        }else {
            PageHelper.startPage(pageNum,pageSize).setReasonable(true);
        }
    }

    /**
     * 返回
     * @param e
     * @return
     */
    public static String getOrderBy(HttpServletRequest request, HttpServletResponse response, String e){
        if (request.getParameter(e) == null || "".equals(request.getParameter(e))){
            return "";
        }else {
            return request.getParameter(e);
        }
    }
    /**
     * 返回给前端分页后的封装对象
     * @param list 数据
     * @return ListByPage
     */
    public ListByPage getList( List<?> list){
        ListByPage listByPage = new ListByPage();
        listByPage.setCode(200);
        listByPage.setMsg("查询成功");
        listByPage.setList(list);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        listByPage.setTotal(pageInfo.getTotal());
        return listByPage;
    }
    /**
     * 返回给前端分页后的封装对象
     * @param msg 信息
     * @param list 数据
     * @return ListByPage
     */
    public ListByPage getList(String msg, List<?> list){
        ListByPage listByPage = new ListByPage();
        listByPage.setCode(200);
        listByPage.setMsg(msg);
        listByPage.setList(list);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        listByPage.setTotal(pageInfo.getTotal());
        return listByPage;
    }
    /**
     * 返回给前端分页后的封装对象
     * @param code 状态码
     * @param msg 信息
     * @param list 数据
     * @return ListByPage
     */

    public ListByPage getList(Integer code ,String msg, List<?> list){
        ListByPage listByPage = new ListByPage();
        listByPage.setCode(code);
        listByPage.setMsg(msg);
        listByPage.setList(list);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        listByPage.setTotal(pageInfo.getTotal());
        return listByPage;
    }
    public ListByPage getList(Integer code ,String msg){
        ListByPage listByPage = new ListByPage();
        listByPage.setCode(code);
        listByPage.setMsg(msg);
        return listByPage;
    }

}
