package com.xuzd.user.entity;


import com.xuzd.user.model.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Entity
@ApiModel(description = "用户信息表")
@Table(name = "lock_user")
public class User {

    @Id
    @ApiModelProperty(value = "用户唯一编号")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 20, message = "用户账号名称最大长度为20个字符")
    @NotEmpty(message = "用户账号名称不能为空")
    @ApiModelProperty(value = "用户账号名称", required = true)
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String password;

    @Length(max = 20, message = "用户姓名最大长度为20个字符")
    //@NotEmpty(message = "用户姓名不能为空")
    @ApiModelProperty(value = "用户姓名", required = true)
    private String userName; // 用户姓名


    @NotEmpty(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话", required = true)
    private String phone;

    @Length(max = 30, message = "邮箱最大长度为30个字符")
    //@NotEmpty(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @ApiModelProperty(value = "账号创建时间")
    private Date createDate;

    @ApiModelProperty(value = "账号状态")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginDate;

    @ApiModelProperty(value = "上次登录时ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "用户头像")
    private byte[] headShot;

    @ApiModelProperty(value = "用户类型 0为普通用户 1为管理员")
    private Integer role;

    @ApiModelProperty(value = "用于展示的时间")
    private String dateFormat;

    @ApiModelProperty(value = "用户余额")
    private Long balance;
}
