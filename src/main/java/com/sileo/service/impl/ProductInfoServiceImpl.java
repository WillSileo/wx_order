package com.sileo.service.impl;

import com.sileo.dataobject.ProductInfo;
import com.sileo.dto.CartDTO;
import com.sileo.enums.ProductStatusEnum;
import com.sileo.enums.ResultEnum;
import com.sileo.exception.SellException;
import com.sileo.repository.ProductInfoRepository;
import com.sileo.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        Page<ProductInfo> productInfoPage = repository.findAll(pageable);
        return productInfoPage;
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        log.info("cartDTOList {}",cartDTOList);
        ProductInfo productInfo = null;
        for (CartDTO cartDTO: cartDTOList) {
             productInfo = repository.findOne(cartDTO.getProductId());
            if(productInfo == null)
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            Integer num = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(num < 0)
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            productInfo.setProductStock(num);
            repository.save(productInfo);
        }
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        log.info("cartDTOList {}",cartDTOList);
        for (CartDTO c: cartDTOList) {
            ProductInfo productInfo = repository.findOne(c.getProductId());
            if(productInfo == null)
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            Integer num = productInfo.getProductStock() + c.getProductQuantity();
            productInfo.setProductStock(num);
            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo updateById(ProductInfo productInfo) {
        return null;
    }
}
