package com.shubham.hardware.repo;

import com.shubham.hardware.entities.Category;
import com.shubham.hardware.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product> findByTitleContaining(String keywords, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);

    Page<Product> findByStockTrue(Pageable pageable);

    Page<Product> findByCategory(Category category,Pageable pageable);

//    other method
//    custom finder method
//    query method
}
