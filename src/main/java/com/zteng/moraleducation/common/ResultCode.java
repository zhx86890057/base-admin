package com.zteng.moraleducation.common;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),

    LOGIN_ERROR(400, "账号未注册或者密码错误"),
    USER_DISABLED(401, "账号已被禁用"),
    USER_EXISTS(402, "用户已存在"),
    UNAUTHORIZED(403, "暂未登录或token已经过期"),
    FORBIDDEN(404, "当前访问没有权限"),
    VALIDATE_FAILED(405, "参数检验失败"),
    DUPLICATE_KEY(406, "插入重复数据"),
    NOT_EXIST(407, "数据不存在"),

    CODE_EXPIRED_WRONG(501, "验证码过期或错误"),
    MESSAGE_SEND_FAIL(503, "短信发送失败"),

    ;


    private int code;
    private String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
