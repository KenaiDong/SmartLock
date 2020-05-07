package com.xuzd.user.entity;


import com.xuzd.user.model.AccountStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@ApiModel(description = "用户消费记录表")
@Table(name = "lock_account_record")
public class AccountRecord {

    @Id
    @ApiModelProperty(value = "记录唯一编号")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "财务变更状态")
    private AccountStatus status;

    @ApiModelProperty(value = "记录信息")
    private String info;

    @ApiModelProperty(value = "变更金额，单位(元)")
    private Long amount;

    @ApiModelProperty(value = "变更之后总余额")
    private Long balance;

    @ApiModelProperty(value = "时间")
    private Date time;
}
