package com.sileo.controller;

import com.sileo.VO.ProductInfoVO;
import com.sileo.VO.ProductVO;
import com.sileo.VO.ResultVO;
import com.sileo.dataobject.ProductCategory;
import com.sileo.dataobject.ProductInfo;
import com.sileo.service.ProductCategoryService;
import com.sileo.service.ProductInfoService;
import com.sileo.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ResultVO getProductUpList(){
        ResultVO<ProductVO> resultVO = new ResultVO<ProductVO>();
        //查询所有上架的产品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();
        //查询上架的商品种类
        List<Integer> categoryList = productInfoList.stream().
                map(e -> e.getCategoryType()).distinct().collect(Collectors.toList());
        List<ProductCategory> cates = new ArrayList<>();
        for (Integer item: categoryList) {
            ProductCategory p = productCategoryService.findByCategoryType(item);
            cates.add(p);
        }

        //数据拼装
        List<ProductVO> list = new ArrayList<ProductVO>();
        for (ProductCategory productCategory : cates){
            ProductVO productVO = new ProductVO();
            productVO.setProductName(productCategory.getCategoryName());
            productVO.setProductType(productCategory.getCategoryType());
            List<ProductInfoVO> infoVOList = new ArrayList<ProductInfoVO>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productVO.getProductType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    infoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(infoVOList);
            list.add(productVO);
        }

        return ResultVOUtil.success(list);
    }
}
