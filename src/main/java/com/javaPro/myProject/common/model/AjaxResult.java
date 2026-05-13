package com.javaPro.myProject.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class AjaxResult extends HashMap<String,Object> {
    /**
     * 状态码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    AjaxResult(int code, String msg){
        super.put("code",code);
        super.put("msg",msg);
    }
    AjaxResult(int code, String msg, Object data){
        super.put("code",code);
        super.put("msg",msg);
        super.put("data",data);
    }

    public AjaxResult() {

    }

    /**
     *增删改成功
     */
    public static AjaxResult ok(){
        return new AjaxResult(200,"操作成功");
    }
    public static AjaxResult ok(String msg){
        return new AjaxResult(200,msg);
    }
    public static AjaxResult ok(String msg ,Object data){
        return new AjaxResult(200,msg,data);
    }
    public static AjaxResult ok(Object data){
        return new AjaxResult(200,"操作成功",data);
    }
    /**
     *增删改失败
     */
    public static AjaxResult error(){
        return new AjaxResult(500,"操作失败");
    }
    public static AjaxResult error(String msg){
        return new AjaxResult(500,msg);
    }
    public static AjaxResult error(String msg ,Object data){
        return new AjaxResult(500,msg,data);
    }
    /**
     * 自定义code
     */
    public static AjaxResult other(int code,String msg ,Object data){
        return new AjaxResult(code,msg,data);
    }
    /**
     * 自动根据int类型判断成功还是错误信息
     */
    public static AjaxResult autoJudge(int count){
        if (count > 0){
            return AjaxResult.ok("操作成功");
        }else {
            return AjaxResult.error("操作失败");
        }
    }
}
