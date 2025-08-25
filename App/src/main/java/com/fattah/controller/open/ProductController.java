package com.fattah.controller.open;

import com.fattah.dto.product.LimitedProductDto;
import com.fattah.dto.product.ProductCategoryDto;
import com.fattah.dto.product.ProductDto;
import com.fattah.enums.ProductType;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.model.ApiResponse;
import com.fattah.service.product.ProductCategoryService;
import com.fattah.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("category")
    public ApiResponse<List<ProductCategoryDto>> getAllCategory(){
        return ApiResponse.<List<ProductCategoryDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(productCategoryService.readAllCategories())
                .build();
    }
    @GetMapping("top/{type}")
    public ApiResponse<List<LimitedProductDto>> get6TopProducts(@PathVariable ProductType type){
        return ApiResponse.<List<LimitedProductDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(productService.read6TopProducts(type))
                .build();
    }
    @GetMapping("{id}")
    public ApiResponse<ProductDto> getProductById(@PathVariable Long id){
        try {
            return ApiResponse.<ProductDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(productService.readProductById(id))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<ProductDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }


}
