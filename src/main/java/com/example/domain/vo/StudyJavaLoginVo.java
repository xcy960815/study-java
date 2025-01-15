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

    String getPassword(){
        return password;
    }

    void setPassword(String password){
        this.password = password;
    }

    String getUsername(){
        return username;
    }

    void  setUsername(String username){
        this.username = username;
    }

}
