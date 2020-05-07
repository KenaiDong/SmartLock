package com.xuzd.lock.service;

import com.xuzd.lock.dao.ILockDao;
import com.xuzd.lock.entity.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class LockService {

    @Autowired
    private ILockDao lockDao;

    /**
     * 注册一个新的锁
     * @param lock
     * @return
     */
    @Transactional
    public Lock addLock(Lock lock){
        return lockDao.save(lock);
    }

    public Lock findLockById(Long id){
        return lockDao.findById(id);
    }

    /**
     * 查询附近的锁信息，暂时只查询坐标距离最近的一个锁信息
     * @return
     */
    public Lock findNearbyLock(String lng, String lat){
        List<Lock> locks = lockDao.findAllByStatus("1");
        Lock result = new Lock();
        result.setLng("0");
        result.setLat("0");
        BigDecimal error = new BigDecimal(lng).add(new BigDecimal(lat));
        // 这个逻辑中，锁列表不能为空
        for (Lock lock : locks) {
            BigDecimal lngError = new BigDecimal(lock.getLng()).subtract(new BigDecimal(lng));
            BigDecimal latError = new BigDecimal(lock.getLat()).subtract(new BigDecimal(lat));
            BigDecimal sumError = lngError.abs().add(latError.abs());
            if (sumError.compareTo(error) == -1){
                result = lock;
                error = sumError;
            }
        }
        return result;
    }
}
