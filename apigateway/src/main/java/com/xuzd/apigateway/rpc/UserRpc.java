package com.xuzd.apigateway.rpc;


import com.xuzd.commons.entity.User;
import com.xuzd.commons.model.JsonModel;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user")
public interface UserRpc {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    JsonModel login(@RequestParam("userName") User user);


}
