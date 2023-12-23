package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.*;
import com.shubham.hardware.services.CategoryService;
import com.shubham.hardware.services.FileService;
import com.shubham.hardware.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/shubham-hardware/categories")
@Tag(name = "CategoryController",description = "REST APIS related to perform category operations!!")
public class CategoryControllers {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imagePathFolder;

    private Logger logger= LoggerFactory.getLogger(CategoryControllers.class);

//    create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto category = categoryService.create(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

//    update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("categoryId") String id){
        CategoryDto updateCategory = categoryService.update(categoryDto,id);
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

//    delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String id){
        categoryService.delete(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Category is deleted successfully!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

//    get category by id
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("categoryId") String id){
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

//    get all categories
//    http://localhost:8086/shubham-hardware/categories?pageNumber=0&pageSize=2&sortBy=[anyAttributeName like name,email,gender]&sortDir=desc
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<CategoryDto> response = categoryService.getAllCategory(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    search categories
//    http://localhost:8086/shubham-hardware/categories/search/u
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategories(@PathVariable("keyword") String keyword){
        List<CategoryDto> list = categoryService.getAllCategoryByTitle(keyword);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }


//    upload category image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(
            @RequestParam("imageName") MultipartFile image,
            @PathVariable("categoryId") String id
    ) throws IOException {
        String imageName = fileService.uploadFile(image,imagePathFolder);

//        get category by id to update category image
        CategoryDto categoryDto = categoryService.getCategoryById(id);

////        delete category image first before updating category image if exist
//        String imageNameToRemoveFromFolder=categoryDto.getCoverImage();
//        logger.info("Image name to remove from folder : {}",imageNameToRemoveFromFolder);
//
//        String fullPathWithImageName=imagePathFolder+imageNameToRemoveFromFolder;// images/category/5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
//        Path path= Paths.get(fullPathWithImageName);
//        logger.info("Path : {}",path);
//        try {
//            Files.delete(path);
//        } catch(InvalidPathException e){
//            logger.info("Category image not found in folder!! : {}",e.getMessage());
//        } catch (NoSuchFileException e) {
//            logger.info("Category image not found in folder!! : {}",e.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//        }
//        update category image
            categoryDto.setCoverImage(imageName);
            categoryService.update(categoryDto, id);
        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("Image updated successfully!!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //    serve category image
    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(
            @PathVariable("categoryId") String id,
            HttpServletResponse response
    ) throws IOException {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        logger.info("Category image name : {}",categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(imagePathFolder,categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }


//    create product with category
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @Valid @RequestBody ProductDto productDto,
            @PathVariable("categoryId") String id
            ){
        ProductDto product = productService.createProductWithCategory(productDto,id);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //    assigning category to existing products
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> assignCategoryToExistingProduct(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    ){
        ProductDto product = productService.addCategoryInsideExistingProduct(productId,categoryId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

//    get all products to given category
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfGivenCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProductsOfGivenCategory = productService.getAllProductsOfGivenCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProductsOfGivenCategory,HttpStatus.OK);
    }
}

