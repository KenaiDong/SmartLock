package com.xuzd.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@ApiModel(description = "图片表")
@Table(name = "photo")
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @ApiModelProperty(value = "图片ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "图片名")
    private String name;

    @ApiModelProperty(value = "图片拓展名")
    private String extension;

    private byte[] fileStream;
}
