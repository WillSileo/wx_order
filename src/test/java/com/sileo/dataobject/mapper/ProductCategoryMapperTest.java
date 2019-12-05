package com.sileo.dataobject.mapper;


import com.sileo.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



import java.util.HashMap;
import java.util.Map;



/**
 * Created by ASUS on 2019/11/20.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductCategoryMapperTest {

    @Autowired
    private ProductCategoryMapper repository;

    @Test
    public void testInsertByMap(){
       Map map = new HashMap<String,Object>();
       map.put("categoryName","男生最爱");
       map.put("categoryType",110);
       int result = repository.insertByMap(map);
       System.out.println(result);

    }

    @Test
    public  void testFindByCategoryType(){
        ProductCategory productCategory = repository.findByCategoryType(110);
        System.out.println(productCategory);
    }

    @Test
    public  void testUpdateByObject(){
        ProductCategory productCategory = new ProductCategory("男生最爱",110);
        int result = repository.updateByObject(productCategory);
        System.out.println(result);
    }

}