package com.fattah.controller.panel.product;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CRUDController;
import com.fattah.dto.product.ProductDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.product.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/product")
public class ProductPanelController implements CRUDController<ProductDto> {
    private final ProductService service;

    public ProductPanelController(ProductService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_product")
    public ApiResponse<ProductDto> add(ProductDto productDto) {
        try {
            return ApiResponse.<ProductDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(productDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ProductDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("delete_product")
    public ApiResponse<Boolean> delete(Long id) {
      return   ApiResponse.<Boolean>builder()
                .message("deleted")
                .status(WebStatus.Success)
                .data(service.delete(id))
                .build();
    }

    @Override
    @CheckPermission("list_product")
    public ApiPanelResponse<List<ProductDto>> getAll(Integer page, Integer size) {
        Page<ProductDto> data=service.readAll(page,size);
        return ApiPanelResponse.<List<ProductDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalPages(data.getTotalPages())
                .totalCounts(data.getTotalElements())
                .build();
    }

    @Override
    @CheckPermission("edit_product")
    public ApiResponse<ProductDto> edit(ProductDto productDto) {
        try {
            return ApiResponse.<ProductDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(productDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ProductDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @GetMapping("cat/{cid}")
    @CheckPermission("list_product")
    public ApiPanelResponse<List<ProductDto>> getAllByCategory(@PathVariable Long cid, Integer page, Integer size) {
        Page<ProductDto> data= null;
        try {
            data = service.readAllByCategory(cid,page,size);
        } catch (ValidationException e) {
            return ApiPanelResponse.<List<ProductDto>>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .totalPages(null)
                    .totalCounts(null)
                    .build();
        }
        return ApiPanelResponse.<List<ProductDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalPages(data.getTotalPages())
                .totalCounts(data.getTotalElements())
                .build();
    }

}
