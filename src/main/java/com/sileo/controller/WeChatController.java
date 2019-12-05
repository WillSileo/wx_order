package com.sileo.controller;


import com.sileo.config.ProjectUrlConfig;
import com.sileo.enums.ResultEnum;
import com.sileo.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WeChatController {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    //授权
    /**
    *@Author: sileo
    *@param: returnUrl
    *@return: string
    *@Description:  获取code
    */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl")String returnUrl){
        //这个是转跳到去获取用户信息的url
        String url = projectUrlConfig.getWechatMpAuthorize() + "/sell/wechat/userInfo";
        //returnUrl 是获取到用户信息后的转发
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】 redirectUrl={}",redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    /**
    *@Author: sileo on
    *@param:  code,returnUrl
    *@return:
    *@Description:  获取用户openid,通过跳转获取用户消息
    */
    public String getUserInfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try{
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e){
            log.error("【微信网页授权】 {}",e);
            throw new  SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("openid={}",openId);
        return "redirect:" + returnUrl + "?openid="+openId;
    }
}
