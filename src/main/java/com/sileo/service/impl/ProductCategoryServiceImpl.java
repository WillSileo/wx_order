package com.sileo.service.impl;

import com.sileo.dataobject.ProductCategory;
import com.sileo.dataobject.mapper.ProductCategoryMapper;
import com.sileo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper repository;
    @Override
    public boolean insertByMap(Map<String, Object> map) {
        int count = repository.insertByMap(map);
        if(count == 1)
            return true;
        return false;
    }

    @Override
    public boolean insertByObject(ProductCategory productCategory) {
        int count = repository.insertByObject(productCategory);
        if(count == 1)
            return true;
        return false;
    }

    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        return repository.findByCategoryType(categoryType);
    }

    @Override
    public List<ProductCategory> findByCategoryName(String categoryName) {
        return repository.findByCategoryName(categoryName);
    }

    public ProductCategoryServiceImpl() {
        super();
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean updateByObject(ProductCategory productCategory) {
        int count = repository.updateByObject(productCategory);
        if(count == 1)
            return true;
        return false;
    }



    @Override
    public boolean deleteByCategoryType(Integer categoryType) {
        int count = repository.deleteByCategoryType(categoryType);
        if(count == 1)
            return true;
        return false;
    }
}
