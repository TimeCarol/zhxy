package com.timecarol.zhxy.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户登录表单信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginForm {
    private String username;
    private String password;
    private String verifiCode;
    private Integer userType;
}
