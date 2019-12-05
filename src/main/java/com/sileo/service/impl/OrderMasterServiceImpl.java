package com.sileo.service.impl;

import com.sileo.convert.OrderMaster2OrderDTOConverter;
import com.sileo.dataobject.OrderDetail;
import com.sileo.dataobject.OrderMaster;
import com.sileo.dataobject.ProductInfo;
import com.sileo.dto.CartDTO;
import com.sileo.dto.OrderDTO;
import com.sileo.enums.OrderStatusEnum;
import com.sileo.enums.PayStatusEnum;
import com.sileo.enums.ResultEnum;
import com.sileo.exception.SellException;
import com.sileo.repository.OrderDetailRepository;
import com.sileo.repository.OrderMasterRepository;
import com.sileo.repository.ProductInfoRepository;
import com.sileo.service.OrderMasterService;
import com.sileo.service.ProductInfoService;
import com.sileo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductInfoService productInfoService;

    @Override
    @Transactional  //保证事务同时成功或失败
    public OrderDTO create(OrderDTO orderDTO) {
        //生成orderId
        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //传进来的只有  商品id以及购买数量
        //1.查询商品（数量、价格）
        for (OrderDetail detail : orderDTO.getOrderDetailList()) {
              ProductInfo productInfo = productInfoRepository.findOne(detail.getProductId());
              //当查询不到相应的产品后，抛出异常
              if(productInfo == null)
                  throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);


              //计算订单总价
              orderAmount = productInfo.getProductPrice().
                    multiply(new BigDecimal(detail.getProductQuantity())).add(orderAmount);

              //2.订单详情入库
              BeanUtils.copyProperties(productInfo,detail);
              detail.setOrderId(orderId);
              detail.setDetailId(KeyUtil.getUniqueKey());
              orderDetailRepository.save(detail);

        }

        //3.创建订单
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterRepository.save(orderMaster);

        //4.扣除库存
        List<CartDTO> cartDTOS = orderDTO.getOrderDetailList().stream().
                map(e -> new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.decreaseStock(cartDTOS);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderDTO orderDTO = new OrderDTO();
        OrderMaster master = orderMasterRepository.findOne(orderId);
        if (master == null)
            throw new  SellException(ResultEnum.ORDER_NOT_EXIST);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetails))
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        BeanUtils.copyProperties(master,orderDTO);
        orderDTO.setOrderDetailList(orderDetails);
        return orderDTO;
    }

    @Override
    public OrderDTO cancle(OrderDTO orderDTO) {
        log.info("【取消订单】入参：orderDTO：{}",orderDTO);
        OrderMaster orderMaster = new OrderMaster();

        //判断订单状态
        //只有新订单，才可以被取消
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) ){
            log.error("【取消订单】 订单状态不正确，orderId：{},orderStatus：{}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //改变订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【订单取消】 订单取消失败，OrderMaster：{}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【订单取消】 订单无商品详情，orderDTO：{}",orderDTO);
            throw  new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e -> new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);
        //退款
        //TODO
        log.info("【取消订单】出参：orderDTO{}",orderDTO);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList);
    }

    @Override
    public OrderDTO finsh(OrderDTO orderDTO) {
        log.info("【完结订单】 入参 :orderDTO：{}",orderDTO);
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            //只有新建订单才能被完结
            log.error("【完结订单】 订单状态不正确  orderId：{},orderStatus：{}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【完结订单】 更新失败，OrderMaster：{}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        log.info("【完结订单】 出参 :orderDTO：{}",orderDTO);
        return orderDTO;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        log.info("【支付订单】 入参 :orderDTO：{}",orderDTO);
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【支付订单】 订单状态错误，orderId：{},orderStatus：{}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【支付订单】 支付状态错误，orderId：{},payStatus：{}",orderDTO.getOrderId(),orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【支付订单】 更新失败，OrderMaster：{}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        log.info("【支付订单】 出参 :orderDTO：{}",orderDTO);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        return null;
    }
}
