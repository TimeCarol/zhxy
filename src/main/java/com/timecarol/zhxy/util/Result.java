package com.timecarol.zhxy.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局统一返回结果类
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(){}

    // 返回数据
    protected static <T> Result<T> build(T data) {
        return new Result<T>().setData(data);
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        return build(body)
                .setCode(resultCodeEnum.getCode())
                .setMessage(resultCodeEnum.getMessage());
    }

    public static<T> Result<T> ok(){
        return Result.ok(null);
    }

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> fail(){
        return Result.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> Result<T> fail(T data){
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg){
        return this.setMessage(msg);
    }

    public Result<T> code(Integer code){
        return this.setCode(code);
    }

    public boolean isOk() {
        return this.getCode().equals(ResultCodeEnum.SUCCESS.getCode());
    }
}

