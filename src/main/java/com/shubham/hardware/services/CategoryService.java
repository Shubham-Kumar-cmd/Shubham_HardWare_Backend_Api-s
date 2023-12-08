package com.shubham.hardware.services;

import com.shubham.hardware.dtos.CategoryDto;
import com.shubham.hardware.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

//    create
    CategoryDto create(CategoryDto categoryDto);

//    update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

//    delete
    void delete(String categoryId);

//    get one
    CategoryDto getCategoryById(String categoryId);

//    get all
PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

//    search
    List<CategoryDto> getAllCategoryByTitle(String keyword);
}
