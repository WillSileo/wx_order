package com.sileo.service;

import com.sileo.dataobject.ProductInfo;
import com.sileo.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {

    //查询单个商品
    ProductInfo findOne(String productId);
    //添加商品
    ProductInfo save(ProductInfo productInfo);

    //查询所有上架商品
    List<ProductInfo> findUpAll();

    //查询所有商品  分页查询
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo updateById(ProductInfo productInfo);

    //减少库存
    public void decreaseStock(List<CartDTO> cartDTOList);

    //增加库存
    public void increaseStock(List<CartDTO> cartDTOList);
}
