package cn.akfang.berry.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    WRONG_USERNAME_OR_PASSWORD(40102, "用户名或密码错误"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    WX_SERVER_ERROR(50002, "微信服务器异常"),
    QINIU_UPLOAD_ERROR(50003, "七牛-文件上传错误"),
    QINIU_DELETE_ERROR(50004, "七牛-文件删除错误"),
    VIDEO_NOT_EXIST_OR_VISIBLE(50005, "视频不存在或不可见");
    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
