package com.sileo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/weixin")
@Slf4j
public class WeiXinController {
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code){
       log.info("进入auth方法。。。。。。");
       log.info("code = {}",code);
       String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxec511acd6750376c&secret=400f69097e9cc680f230ef6eca021a18&code=" + code + "&grant_type=authorization_code";
       RestTemplate restTemplate = new RestTemplate();
       String response = restTemplate.getForObject(url,String.class);
       log.info("response = {}",response);
    }
}
