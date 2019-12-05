package com.sileo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sileo.dataobject.ProductInfo;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    public List<ProductInfo> findByProductStatus(Integer productStatus);
}
