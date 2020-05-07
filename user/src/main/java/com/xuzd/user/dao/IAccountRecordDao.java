package com.xuzd.user.dao;

import com.xuzd.commons.db.repository.ICustomRepository;
import com.xuzd.user.entity.AccountRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAccountRecordDao extends ICustomRepository<AccountRecord, Long> {

    @Query("from AccountRecord a where a.userId = ?1")
    List<AccountRecord> findAccountRecordsByUserId(Long userId);


}
