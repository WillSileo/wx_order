package com.sileo.service.impl;

import com.sileo.dataobject.ProductInfo;
import com.sileo.dto.CartDTO;
import com.sileo.enums.ProductStatusEnum;
import com.sileo.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoService productInfoService;

    @Test
    public void save() {
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setProductId("123457");
//        productInfo.setProductName("皮皮虾");
//        productInfo.setProductPrice(new BigDecimal(3.2));
//        productInfo.setProductStock(100);
//        productInfo.setProductDescription("很好吃的虾");
//        productInfo.setProductIcon("http://xxxxx.jpg");
//        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
//        productInfo.setCategoryType(2);
//
//        ProductInfo result = productInfoService.save(productInfo);

        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123456");
        productInfo.setProductName("菠萝冰");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很冰很爽");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productInfo.setCategoryType(2);

        ProductInfo result = productInfoService.save(productInfo);


    }

    @Test
    public void findOne() {
        ProductInfo productInfo = productInfoService.findOne("123456");
        System.out.println(productInfo);
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        for (ProductInfo p: productInfoList) {
            System.out.println(p);
        }
    }

    @Test
    public void findAll() {
        PageRequest request = new PageRequest(0, 2);
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);
        Assert.assertNotEquals(0, productInfoPage.getTotalElements());
    }

    @Test
    public void decreaseStock(){
        List<CartDTO> list = new ArrayList<CartDTO>();
        CartDTO c1 = new CartDTO("123456",2);
        CartDTO c2 = new CartDTO("123457",2);
        list.add(c1);
        list.add(c2);
        productInfoService.decreaseStock(list);
    }
}