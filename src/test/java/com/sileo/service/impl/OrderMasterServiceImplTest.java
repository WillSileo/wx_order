package com.sileo.service.impl;

import com.sileo.dataobject.OrderDetail;
import com.sileo.dto.OrderDTO;
import com.sileo.service.OrderMasterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderMasterServiceImplTest {
    @Autowired
    private OrderMasterService orderMasterService;

    private final String BUYER_OPENID = "1101110";

    private final String ORDER_ID = "1574441406130462863";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("sileo");
        orderDTO.setBuyerOpenid(BUYER_OPENID);
        orderDTO.setBuyerPhone("12345678912");
        orderDTO.setBuyerAddress("上海");


        List<OrderDetail> list = new ArrayList<OrderDetail>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123456");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductQuantity(1);
        list.add(o1);
        list.add(o2);

        orderDTO.setOrderDetailList(list);
        OrderDTO result = orderMasterService.create(orderDTO);

        log.info("【创建订单】result {}",result);
    }

    @Test
    public void findOne() {
       OrderDTO orderDTO =  orderMasterService.findOne(ORDER_ID);
       log.info("【订单查询】：{}",orderDTO);
    }

    @Test
    public void cancle() {
        OrderDTO orderDTO = orderMasterService.findOne(ORDER_ID);
        OrderDTO reuslt = orderMasterService.cancle(orderDTO);
        log.info("【订单取消】：{}",orderDTO);
    }
  //1574664303365842506
    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage = orderMasterService.findList(BUYER_OPENID, request);
        List<OrderDTO> list = orderDTOPage.getContent();
        log.info("list = {}",list);
    }

    @Test
    public void finsh() {
        OrderDTO orderDTO = orderMasterService.findOne("1574664303365842506");
        OrderDTO reuslt = orderMasterService.cancle(orderDTO);
        log.info("【订单取消】：{}",orderDTO);
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderMasterService.findOne(ORDER_ID);
        OrderDTO reuslt = orderMasterService.cancle(orderDTO);
        log.info("【订单取消】：{}",orderDTO);
    }

    @Test
    public void findList1() {
    }
}