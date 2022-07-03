package com.timecarol.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@TableName("tb_admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Admin {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Character gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    /* 头像图片路径 */
    private String portraitPath;
}
