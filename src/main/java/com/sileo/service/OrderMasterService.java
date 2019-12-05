package com.sileo.service;


import com.sileo.dto.OrderDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface OrderMasterService {
    //创建订单
    OrderDTO create(OrderDTO orderDTO);

    //查询单个订单
    OrderDTO findOne(String orderId);

    //取消订单
    OrderDTO cancle(OrderDTO orderDTO);

    //查询订单列表
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    //完结订单
    OrderDTO finsh(OrderDTO orderDTO);

    //支付订单
    OrderDTO paid(OrderDTO orderDTO);

    //查询订单列表
    Page<OrderDTO> findList(Pageable pageable);

}
