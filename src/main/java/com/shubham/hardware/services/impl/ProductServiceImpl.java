package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.ProductDto;
import com.shubham.hardware.entities.Product;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.helper.Helper;
import com.shubham.hardware.repo.ProductRepository;
import com.shubham.hardware.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${product.profile.image.path}")
    private String productImageFolder;

    private Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Override
    public ProductDto create(ProductDto productDto) {
//        generate product id
        String productId= UUID.randomUUID().toString();
        productDto.setProductId(productId);

//        date add
        productDto.setAddedDate(new Date());
        Product product = modelMapper.map(productDto, Product.class);
        Product productCreated = productRepository.save(product);
        return modelMapper.map(productCreated, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id!!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());

        product.setAddedDate(productDto.getAddedDate());
//        on updating product Date is also updated
//        product.setAddedDate(new Date());

        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImage(productDto.getProductImage());
//        update product
        Product productUpdated = productRepository.save(product);
        return modelMapper.map(productUpdated, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id!!"));

//        delete product image first before deleting product
        String imageName=product.getProductImage();
        String fullPathWithImageName=productImageFolder+imageName;// images/products/5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
        logger.info("Full path with image name : {}",fullPathWithImageName);
        try {
            Path path= Paths.get(fullPathWithImageName);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("Product image not found in folder!! : {}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        delete product
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product not found with given id!!"));
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
        return response;
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir){
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> list = Helper.getPageableResponse(page, ProductDto.class);
        return list;
    }

    @Override
    public PageableResponse<ProductDto> getAllProductByTitle(String keyword,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(keyword,pageable);
        PageableResponse<ProductDto> list = Helper.getPageableResponse(page, ProductDto.class);
        return list;
    }
}