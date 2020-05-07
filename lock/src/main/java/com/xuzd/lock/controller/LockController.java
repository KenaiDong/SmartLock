package com.xuzd.lock.controller;

import com.alibaba.fastjson.JSONObject;
import com.xuzd.commons.model.JsonModel;
import com.xuzd.lock.entity.Lock;
import com.xuzd.lock.service.LockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Api("智能锁管理")
@Controller
@Configuration
public class LockController {

    @Autowired
    LockService lockService;

    @RequestMapping(value = "/lockInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getLockInfo(@RequestParam Long id){
        Lock lock = lockService.findLockById(id);
        if (ObjectUtils.isEmpty(lock)){
            JSONObject obj = new JSONObject();
            obj.put("error","无此id所对应的智能锁");
            return obj;
        }
        return (JSONObject) JSONObject.toJSON(lock);
    }

    @RequestMapping(value = "/addLock", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel addLock(Lock lock){
        try {
            // 判断坐标轴数据是否都为数字
            BigDecimal coords = new BigDecimal(lock.getLng()).add(new BigDecimal(lock.getLat()));
            if (ObjectUtils.isEmpty(lock.getStatus())) lock.setStatus("1");
            lockService.addLock(lock);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, "数据请求错误");
        }
        return new JsonModel(true, "智能锁添加成功");
    }

    @RequestMapping(value = "/getNearbyLock", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getNearbyLock(String lng, String lat){
        Lock lock = lockService.findNearbyLock(lng, lat);
        return (JSONObject) JSONObject.toJSON(lock);
    }
}
