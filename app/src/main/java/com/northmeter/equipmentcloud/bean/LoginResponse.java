package com.northmeter.equipmentcloud.bean;

/**
 * Created by dyd on 2019/1/4.
 * 登录对象
 */

public class LoginResponse extends CommonResponse{
    private int expire;
    private String token;

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
