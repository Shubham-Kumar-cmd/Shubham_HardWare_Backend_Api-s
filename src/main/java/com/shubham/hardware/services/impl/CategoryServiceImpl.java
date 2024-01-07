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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "category")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.profile.image.path}")
    private String categoryImageFolder;

    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    @CachePut(key = "#categoryDto.categoryId")
    public CategoryDto create(CategoryDto categoryDto) {
        logger.info("CategoryService::create() connecting to database!!");
//        generate unique id in string format
        String categoryId = UUID.randomUUID().toString();
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCategoryId(categoryId);
        Category categoryCreated = categoryRepository.save(category);
        CategoryDto categoryDtoCreated = modelMapper.map(categoryCreated, CategoryDto.class);
        return categoryDtoCreated;
    }

    @Override
    @CachePut(key = "#categoryId")
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        logger.info("CategoryService::update() connecting to database!!");
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category categoryUpdated = categoryRepository.save(category);
        CategoryDto categoryDtoUpdated = modelMapper.map(categoryUpdated, CategoryDto.class);
        return categoryDtoUpdated;
    }

    @Override
    @CacheEvict(key = "#categoryId")
    public void delete(String categoryId) {
        logger.info("CategoryService::delete() connecting to database!!");
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));

//        delete Category image first before deleting user
        String imageName=category.getCoverImage();
        String fullPathWithImageName=categoryImageFolder+imageName;// images/users/5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
        logger.info("Full path with image name : {}",fullPathWithImageName);
        try {
            Path path= Paths.get(fullPathWithImageName);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("Category image not found in folder!! : {}",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        delete category
        categoryRepository.delete(category);
    }

    @Override
    @Cacheable(key = "#categoryId")
    public CategoryDto getCategoryById(String categoryId) {
        logger.info("CategoryService::getCategoryById() connecting to database!!");
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found with given id!!"));
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
        return categoryDto;
    }

    @Override
    @Cacheable
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("CategoryService::getAllCategory() connecting to database!!");

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
