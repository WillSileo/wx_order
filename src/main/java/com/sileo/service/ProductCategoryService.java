package com.sileo.service;

import com.sileo.dataobject.ProductCategory;

import java.util.List;
import java.util.Map;

public interface ProductCategoryService {
    boolean insertByMap(Map<String,Object> map);
    boolean insertByObject(ProductCategory productCategory);
    ProductCategory findByCategoryType(Integer categoryType);
    List<ProductCategory> findByCategoryName(String categoryName);
    List<ProductCategory> findAll();
    boolean updateByObject(ProductCategory productCategory);

    boolean deleteByCategoryType(Integer categoryType);
}
