package com.xuzd.lock.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel(description = "智能锁信息表")
@Table(name = "lock_info")
public class Lock {

    @Id
    @ApiModelProperty(value = "智能锁唯一编号")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "经度")
    private String lng;

    @ApiModelProperty(value = "纬度")
    private String lat;

    @ApiModelProperty(value = "锁信息")
    private String info;

    @ApiModelProperty(value = "锁所在城市")
    private String city;

    @ApiModelProperty(value = "锁状态 0:异常 1:正常")
    private String status;

}
