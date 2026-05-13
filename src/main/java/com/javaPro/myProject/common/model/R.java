package com.javaPro.myProject.common.model;


import lombok.Data;

@Data
public class R {

    // 返回的编号
    private Integer code;
    // 返回的数据,数据类型N中， JSON 格式     传递给前端
    private Object data;
    // 返回的信息
    private String message;

    // 调用过程中，保持一种调用。直接用类去调用。
    private R() {

    }

    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description 成功返回
     * @Date 21:55 2021/6/23
     * @Param []
     **/
    public static R success(Object data, String message) {
        R r = new R();
        r.setCode(200);
        r.setData(data);
        r.setMessage(message == null ? "操作成功" : message);
        return r;
    }

    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description 成功返回
     * @Date 21:55 2021/6/23
     * @Param []
     **/
    public static R success(Object data) {
        return success(data, null);
    }


    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description
     * @Date 22:03 2021/6/23
     * @Param [code 失败的状态, message 失败的原因]
     **/
    public static R fail(String message) {
        R r = new R();
        r.setCode(500);
        r.setData(null);
        r.setMessage(message);
        return r;
    }

    /**
     * @return com.kuangstudy.common.R
     * @Author xuke
     * @Description
     * @Date 22:03 2021/6/23
     * @Param [code 失败的状态, message 失败的原因]
     **/
    public static R fail() {
        R r = new R();
        r.setCode(500);
        r.setData(null);
        r.setMessage("操作失败");
        return r;
    }
}

