package com.shubham.hardware.repo;

import com.shubham.hardware.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {

    List<Category> findByTitleContaining(String keywords);
}
