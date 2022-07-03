package com.timecarol.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@TableName("tb_grade")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Grade {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String manager;
    private String email;
    private String telephone;
    private String introduction;
}
