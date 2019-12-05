package com.sileo.service;

import com.sileo.dto.OrderDTO;

public interface BuyerService {
    OrderDTO findOrderOne(String openid,String orderId);
    OrderDTO cancle(String openid,String orderId);
}
