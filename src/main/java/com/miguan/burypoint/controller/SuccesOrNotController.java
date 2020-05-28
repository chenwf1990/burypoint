package com.miguan.burypoint.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuccesOrNotController {

    /**
     * Linux运维检查是否重启成功服务接口
     * @return
     */
    @RequestMapping("/succesOrNot")
    public String succesOrNot(){
        return "200";
    }
}
