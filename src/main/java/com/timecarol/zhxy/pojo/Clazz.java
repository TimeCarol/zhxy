package com.timecarol.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@TableName("tb_clazz")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class Clazz {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer number;
    private String introduction;
    private String headmaster;
    private String email;
    private String telephone;
    private String gradeName;
}
