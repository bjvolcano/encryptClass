package com.tb.test.controller;

import com.tb.test.component.Components;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bjvolcano
 * @Date 2021/5/11 10:09 上午
 * @Version 1.0
 */
@RestController
public class TestController {
    @Autowired
    private Components components;

    @RequestMapping(value ="/test")
    public String test() {
        components.test();

        return "aasdfsdf";
    }
}
