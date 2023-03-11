package com.dermacon.proxy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/health")
    public String health() {
        String msg = "health ok";
        log.info("msg: {}", msg);
        return msg;
    }

}
