package com.fattah.controller.panel.product;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CreateController;
import com.fattah.controller.base.ReadController;
import com.fattah.controller.base.UpdateController;
import com.fattah.dto.product.ProductCategoryDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.product.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/product/category")
public class ProductCategoryPanelController implements CreateController<ProductCategoryDto>, ReadController<ProductCategoryDto>, UpdateController<ProductCategoryDto> {
    private final ProductCategoryService service;

    @Autowired
    public ProductCategoryPanelController(ProductCategoryService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_product_category")
    public ApiResponse<ProductCategoryDto> add(ProductCategoryDto productCategoryDto) {
        try {
            return ApiResponse.<ProductCategoryDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(productCategoryDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ProductCategoryDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("list_product_category")
    public ApiPanelResponse<List<ProductCategoryDto>> getAll(Integer page, Integer productCategory) {
        Page<ProductCategoryDto> data=service.readAll(page,productCategory);
        return ApiPanelResponse.<List<ProductCategoryDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalPages(data.getTotalPages())
                .totalCounts(data.getTotalElements())
                .build();
    }

    @Override
    @CheckPermission("edit_product_category")
    public ApiResponse<ProductCategoryDto> edit(ProductCategoryDto productCategoryDto) {
        try {
            return ApiResponse.<ProductCategoryDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(productCategoryDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ProductCategoryDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
