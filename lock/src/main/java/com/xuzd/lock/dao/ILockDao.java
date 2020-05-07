package com.xuzd.lock.dao;

import com.xuzd.commons.db.repository.ICustomRepository;
import com.xuzd.lock.entity.Lock;

import java.util.List;

public interface ILockDao extends ICustomRepository<Lock, String > {
    Lock findById(Long id);

    List<Lock> findAllByStatus(String status);

}
