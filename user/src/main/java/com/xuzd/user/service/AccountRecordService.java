package com.xuzd.user.service;

import com.xuzd.user.dao.IAccountRecordDao;
import com.xuzd.user.entity.AccountRecord;
import com.xuzd.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AccountRecordService {
    @Autowired
    IAccountRecordDao accountRecordDao;

    public List<AccountRecord> findAccountRecordById(Long id){
        return accountRecordDao.findAccountRecordsByUserId(id);
    }

    @Transactional
    public AccountRecord addAccountRecord(AccountRecord accountRecord){
        return accountRecordDao.save(accountRecord);
    }
}
