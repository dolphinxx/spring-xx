package com.jagsii.springxx.common.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class R<T> {
    protected T data;
    protected int status;
    protected String msg;

    public R(int status) {
        this(status, null, null);
    }

    public R(int status, String msg) {
        this(status, null, msg);
    }

    public R(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static <T> R<T> success() {
        return new R<>(HttpStatus.OK.value(), null, null);
    }

    public static <T> R<T> success(T data) {
        return new R<>(HttpStatus.OK.value(), data, null);
    }

    public static <T> R<T> failed() {
        return serverError();
    }

    public static <T> R<T> serverError() {
        return new R<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "出错了！");
    }

    public static <T> R<T> clientError() {
        return new R<>(HttpStatus.BAD_REQUEST.value(), "请求失败！");
    }

    public static <T> R<T> failed(int status, String msg) {
        return new R<>(status, msg);
    }
}
