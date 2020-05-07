package com.xuzd.user.service;

import com.xuzd.user.dao.IPhotoDao;
import com.xuzd.user.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PhotoService {
    @Autowired
    IPhotoDao photoDao;

    /**
     * 保存文件
     */
    public void save(Photo photo){
        photoDao.save(photo);
    }

    /**
     * 查询文件
     */
    public Photo findById(Long id){
        return photoDao.findOne(id);
    }
}
