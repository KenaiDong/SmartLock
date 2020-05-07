package com.xuzd.commons.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 *    -100到100为系统默认错误码段
 * 0： 接口调用成功
 * -1： 无数据或数据为空
 * -2： 服务器出现错误
 * -3： 参数错误
 * -4： 权限验证失败
 * -5： 数据库访问异常
 * -6： 接口请求方式错误
 * -100： 其他错误
 *
 */
@Data
@AllArgsConstructor
@ApiModel(description = "响应消息")
public class JsonModel {
    @ApiModelProperty(value = "请求是否成功", required = true)
    private boolean success;

    @ApiModelProperty(value = "提示信息")
    private String msg;

    @ApiModelProperty(value = "错误码")
    private int code;//错误码

    @ApiModelProperty(value = "请求结果")
    private Object obj;

    public JsonModel() {

    }

    public static JsonModel success(){
        return new JsonModel(true);
    }

    public static JsonModel error(String msg, int code){
        JsonModel res = new JsonModel(false, msg);
        res.setCode(code);
        return res;
    }

    public static JsonModel error(String msg){
        return new JsonModel(false, msg);
    }

    public static JsonModel of(boolean success, String msg, Object obj, int code){
        return new JsonModel(success, msg, obj, code);
    }

    public JsonModel(boolean success, String msg, Object obj, int code) {
        setModel(success,msg,obj,code);
    }

    public JsonModel(boolean success, String msg, Object obj) {
        this.success = success;
        this.msg = msg;
        this.obj = obj;

    }

    public JsonModel(boolean success, Object obj) {
        this.success = success;
        this.obj = obj;

    }

    public JsonModel(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public JsonModel(boolean success) {
        this.success = success;
    }

    public JsonModel success(Object obj){
        setModel(true,"请求成功",obj,0);
        return this;
    }

    public JsonModel nodata(Object obj){
        setModel(true,"无数据",obj,-1);
        return this;
    }

    public JsonModel serverError(Object obj){
        setModel(false,"服务器异常",obj,-2);
        return this;
    }

    public JsonModel parameError(Object obj){
        setModel(false,"参数错误",obj,-3);
        return this;
    }

    public JsonModel authorityError(Object obj){
        setModel(false,"权限验证失败",obj,-4);
        return this;
    }

    public JsonModel databaseError(Object obj){
        setModel(false,"数据库访问异常",obj,-5);
        return this;
    }

    public JsonModel other(Object obj){
        setModel(false,"未知异常",obj,-100);
        return this;
    }

    private void setModel(boolean success, String msg, Object obj, int code){
        this.success = success;
        this.msg = msg;
        this.obj = obj;
        this.code = code;
    }


}
