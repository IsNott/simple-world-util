package io.github.nott.model;

import lombok.Data;

/**
 * @author Nott
 * @date 2024-8-1
 */
@Data
public class R {

    private Integer code;

    private String msg;


    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static R ok() {
        return new R(200, "success");
    }

    public static R ok(String msg) {
        return new R(200, msg);
    }

    public static R fail() {
        return new R(-999, "fail");
    }

    public static R fail(Integer code) {
        return new R(code, "fail");
    }

    public static R fail(String msg) {
        return new R(-999, msg);
    }
}
