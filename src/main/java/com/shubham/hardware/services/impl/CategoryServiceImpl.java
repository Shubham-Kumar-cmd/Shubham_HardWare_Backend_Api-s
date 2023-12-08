package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.CategoryDto;
import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.entities.Category;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.helper.Helper;
import com.shubham.hardware.repo.CategoryRepository;
import com.shubham.hardware.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
//        generate unique id in string format
        String categoryId = UUID.randomUUID().toString();
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCategoryId(categoryId);
        Category categoryCreated = categoryRepository.save(category);
        CategoryDto categoryDtoCreated = modelMapper.map(categoryCreated, CategoryDto.class);
        return categoryDtoCreated;
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category categoryUpdated = categoryRepository.save(category);
        CategoryDto categoryDtoUpdated = modelMapper.map(categoryUpdated, CategoryDto.class);
        return categoryDtoUpdated;
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {

//        ternary operator
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);

//        List<Category> categories = page.getContent();
////        using stream api
//        List<CategoryDto> dtoList = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
//
//        PageableResponse<CategoryDto> response = new PageableResponse<>();
//        response.setContent(dtoList);
//        response.setPageNumber(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalElements(page.getTotalElements());
//        response.setTotalPages(page.getTotalPages());
//        response.setLastPage(page.isLast());
//        return response;

//        Aliter
        PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
        return response;
    }

    @Override
    public List<CategoryDto> getAllCategoryByTitle(String keyword) {
        List<Category> categories = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> dtoList = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        return dtoList;
    }
}
