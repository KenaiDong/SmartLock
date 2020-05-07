package com.xuzd.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class testController {

    @RequestMapping(value = "test1/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable String id){
        return id;
    }
}
