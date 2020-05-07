package com.xuzd.user.dao;

import com.xuzd.commons.db.repository.ICustomRepository;
import com.xuzd.user.entity.City;

import java.util.List;

public interface ICityDao extends ICustomRepository<City, String > {

    List<City> findByCity(String city);

}
