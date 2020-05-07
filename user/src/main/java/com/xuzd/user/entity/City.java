package com.xuzd.user.entity;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@ApiModel(description = "地理信息对应表")
@Table(name = "weather_district_id")
public class City {

    @Id
    private String areacode;
    private String districtcode;
    private String cityGeocode;
    private String city;
    private String districtGeocode;
    private String district;
    private String lon;
    private String lat;
    private String staFc;
    private String staRt;
    private String province;
    private String fcLon;
    private String fcLat;
    private String rtLon;
    private String rtLat;
    private String originAreacode;
    private String exclude;

}
