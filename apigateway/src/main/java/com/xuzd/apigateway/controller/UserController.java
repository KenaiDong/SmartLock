package com.xuzd.apigateway.controller;

import com.xuzd.apigateway.rpc.UserRpc;
import com.xuzd.commons.entity.User;
import com.xuzd.commons.model.JsonModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRpc userRpc;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonModel login(@RequestParam User user){
        return userRpc.login(user);
    }
}
