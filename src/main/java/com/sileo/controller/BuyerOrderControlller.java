package com.sileo.controller;

import com.sileo.VO.ResultVO;
import com.sileo.convert.OrderForm2OrderDTOConvert;
import com.sileo.dto.OrderDTO;
import com.sileo.enums.ResultEnum;
import com.sileo.exception.SellException;
import com.sileo.form.OrderForm;
import com.sileo.service.BuyerService;
import com.sileo.service.OrderMasterService;
import com.sileo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderControlller {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
     public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建订单】 参数错误，orderForm = {}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConvert.convert(orderForm);
        //判断购物车是否为空
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】 购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult = orderMasterService.create(orderDTO);
        Map<String,String> map = new HashMap<String,String>();
        map.put("orderId",createResult.getOrderId());
        return  ResultVOUtil.success(map);
     }

     @GetMapping("list")
     public  ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                           @RequestParam(value = "page",defaultValue = "0") Integer page,
                                           @RequestParam(value = "size",defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】 openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = new PageRequest(page,size);
        Page<OrderDTO> orderDTOPage  = orderMasterService.findList(openid,pageRequest);
        return ResultVOUtil.success(orderDTOPage.getContent());
     }


    //取消订单
    @PostMapping("/cancle")
    public ResultVO cancle(@RequestParam(value = "openid") String openid,
                           @RequestParam(value = "orderId") String orderId){
       buyerService.cancle(openid,orderId);
        return ResultVOUtil.success();
    }
    //查询订单细节
    @GetMapping("/detail")
    public ResultVO<OrderDTO> details(@RequestParam(value = "openid") String openid,
                            @RequestParam(value = "orderId") String orderId){
        OrderDTO orderDTO = buyerService.findOrderOne(openid,orderId);
        return  ResultVOUtil.success(orderDTO);
    }





}
