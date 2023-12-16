package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.*;
import com.shubham.hardware.services.FileService;
import com.shubham.hardware.services.ProductService;
import io.swagger.annotations.Api;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/shubham-hardware/products")
@Api(value = "ProductController",description = "REST APIS related to perform product operations!!")
public class ProductControllers {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String productImageFolder;

    private Logger logger= LoggerFactory.getLogger(ProductControllers.class);

    //    create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        ProductDto product = productService.create(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //    update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable("productId") String id){
        ProductDto updateProduct = productService.update(productDto,id);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    //    delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String id){
        productService.delete(id);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Product is deleted successfully!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //    get product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") String id){
        ProductDto productDto = productService.getProductById(id);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //    get all products
//    http://localhost:8086/shubham-hardware/products?pageNumber=0&pageSize=2&sortBy=[anyAttributeName like name,email,gender]&sortDir=desc
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> response = productService.getAllProduct(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    get all : Live
//    http://localhost:8086/shubham-hardware/products/live?pageNumber=0&pageSize=2&sortBy=[anyAttributeName like name,email,gender]&sortDir=desc
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> response = productService.getAllLive(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

//    get all : Stock
//    http://localhost:8086/shubham-hardware/products/stock?pageNumber=0&pageSize=2&sortBy=[anyAttributeName like name,email,gender]&sortDir=desc
    @GetMapping("/stock")
    public ResponseEntity<PageableResponse<ProductDto>> getAllStockProduct(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> response = productService.getAllStock(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //    search products
//    http://localhost:8086/shubham-hardware/products/search/u
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable("query") String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
            ){
        PageableResponse<ProductDto> list = productService.getAllProductByTitle(keyword,pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

//    upload product image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("imageName") MultipartFile image,
            @PathVariable("productId") String productId
    ) throws IOException {
        String imageName = fileService.uploadFile(image,productImageFolder);

//        get product by id to update product image
        ProductDto productDto = productService.getProductById(productId);

//        delete product image first before updating product image if exist
        String imageNameToRemoveFromFolder=productDto.getProductImage();
        logger.info("Image name to remove from folder : {}",imageNameToRemoveFromFolder);

        String fullPathWithImageName=productImageFolder+imageNameToRemoveFromFolder;// images/products/5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
        Path path= Paths.get(fullPathWithImageName);
//        logger.info("Path : {}",path);
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("Product image not found in folder!! : {}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//        update product image
            productDto.setProductImage(imageName);
            productService.update(productDto, productId);
        }
        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("Image updated successfully!!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //    serve product image
    @GetMapping("/image/{productId}")
    public void serveProductImage(
            @PathVariable("productId") String productId,
            HttpServletResponse response
    ) throws IOException {
        ProductDto productDto = productService.getProductById(productId);
        logger.info("Product image name : {}",productDto.getProductImage());
        InputStream resource = fileService.getResource(productImageFolder,productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
