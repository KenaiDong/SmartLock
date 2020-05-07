package com.xuzd.commons.entity;



import com.xuzd.commons.model.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class User {

    private Long id;

    private String loginName;

    private String password;

    private String userName; // 用户姓名

    private String phone;

    private String email;

    private Date createDate;

    private UserStatus status;

    private Date lastLoginDate;

    private String ipAddress;

    private byte[] headShot;
}
