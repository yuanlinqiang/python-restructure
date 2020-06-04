package com.gsafety.pythonrestructure.Test;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/python/api")
public class TestController {

    @RequestMapping(value = "/info/test", method = RequestMethod.GET)
    public String testPing() {
        return "成功接入！！！！！！！！！";
    }

}
