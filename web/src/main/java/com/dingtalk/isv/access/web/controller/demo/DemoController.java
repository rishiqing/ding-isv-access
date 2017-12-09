package com.dingtalk.isv.access.web.controller.demo;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
    @Autowired
    private AsyncEventBus asyncCorpAuthSuiteEventBus;

    @RequestMapping("/demo/testEventBus")
    @ResponseBody
    public String testEventBus(
            @RequestParam(value = "num") Integer num
    ) {
        asyncCorpAuthSuiteEventBus.post(num);
        return "success:--/test: " + num;
    }
}
