package com.xuzd.user.service;

import com.xuzd.user.dao.ICityDao;
import com.xuzd.user.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    ICityDao weatherDistrictIdDao;

    public List<City> findByCity(String city){
        return findByCity(city);
    }
}
