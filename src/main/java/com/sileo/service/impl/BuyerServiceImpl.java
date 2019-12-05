package com.sileo.service.impl;

import com.sileo.dto.OrderDTO;
import com.sileo.enums.ResultEnum;
import com.sileo.exception.SellException;
import com.sileo.repository.OrderMasterRepository;
import com.sileo.service.BuyerService;
import com.sileo.service.OrderMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderMasterService orderMasterService;


    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
       return checkOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancle(String openid, String orderId) {
        OrderDTO orderDTO = checkOwner(openid,orderId);
        if (orderDTO == null){
            log.error("【查询订单】 订单不存在， orderId = {}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        OrderDTO cancleResult = orderMasterService.cancle(orderDTO);
        return cancleResult;
    }

    public OrderDTO checkOwner(String openid, String orderId){
        OrderDTO orderDTO = orderMasterService.findOne(orderId);
        if(orderDTO == null){
            return null;
        }
        if(!openid.equals(orderDTO.getBuyerOpenid())){
            log.error("【查询订单】 订单的openid不一致， openid = {} , orderDTO = {}",openid,orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
