package com.example.exception;

/**
 * 自定义异常
 * 运行时异常
 */
public class CustomerException extends RuntimeException {
    private String code;
    private String msg;

    public CustomerException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CustomerException(String msg) {
        this.msg = msg;
        this.code = "500";
    }
    public CustomerException() {

    }
    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
