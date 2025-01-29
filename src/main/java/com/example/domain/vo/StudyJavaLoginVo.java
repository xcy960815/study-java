package com.example.domain.vo;

//import lombok.Data;

//@Data
public class StudyJavaLoginVo {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String captcha;

    public String getCaptcha(){
        return captcha;
    }

    public void setCaptcha(String captcha){
        this.captcha = captcha;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public void  setUsername(String username){
        this.username = username;
    }

}
