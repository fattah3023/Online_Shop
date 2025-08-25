package com.fattah.controller.panel.site;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CRUDController;
import com.fattah.dto.site.NavBarDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.NavBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/nav")
public class NavPanelController implements CRUDController<NavBarDto> {
    private final NavBarService service;

    @Autowired
    public NavPanelController(NavBarService service) {
        this.service = service;
    }
    @CheckPermission("add_nav")
    @Override
    public ApiResponse<NavBarDto> add(NavBarDto dto) {
        try {
            return ApiResponse.<NavBarDto>builder()
                    .status(WebStatus.Success)
                    .message("OK")
                    .data(service.create(dto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<NavBarDto>builder()
                    .status(WebStatus.Failed)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @CheckPermission("delete_nav")
    @Override
    public ApiResponse<Boolean> delete(Long id) {
    return ApiResponse.<Boolean>builder()
            .message("OK")
            .status(WebStatus.Success)
            .data(service.delete(id))
            .build();
    }


    @CheckPermission("list_nav")
    @Override
    public ApiPanelResponse<List<NavBarDto>> getAll(Integer page, Integer size) {
       Page<NavBarDto> data= service.readAll(page, size);
        return ApiPanelResponse.<List<NavBarDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalCounts(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }

    @CheckPermission("edit_nav")
    @Override
    public ApiResponse<NavBarDto> edit(NavBarDto dto) {
        try {
            return ApiResponse.<NavBarDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(dto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<NavBarDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @CheckPermission("edit_nav")
    @PutMapping("swap-up/{id}")
    public ApiResponse<Boolean> moveUp(@PathVariable Long id) {
        try {
            return ApiResponse.<Boolean>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.swapUp(id))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<Boolean>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @CheckPermission("edit_nav")
    @PutMapping("swap-down/{id}")
    public ApiResponse<Boolean> moveDown(@PathVariable Long id) {
        try {
            return ApiResponse.<Boolean>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.swapDown(id))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<Boolean>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
