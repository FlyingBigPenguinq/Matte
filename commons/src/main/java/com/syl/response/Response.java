package com.syl.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */

@Data
@Accessors(chain = true)
@ApiModel("统一封装的返回类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("消息体")
    private String message;

    @ApiModelProperty("数据")
    private T data;

    public static <T> Response<T> success() {
        return new Response<T>().setCode(HttpStatus.OK.value())
                .setMessage(HttpStatus.OK.getReasonPhrase());
    }

    public static <T> Response<T> success(String msg) {
        return new Response<T>().setCode(HttpStatus.OK.value())
                .setMessage(msg);
    }

    public static <T> Response<T> fail() {
        return new Response<T>().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public static <T> Response<T> fail(String msg) {
        return new Response<T>().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(msg);
    }

    public static <T> Response<T> fail(Integer code, String msg) {
        return new Response<T>().setCode(code)
                .setMessage(msg);
    }


}
