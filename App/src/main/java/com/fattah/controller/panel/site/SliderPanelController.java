package com.fattah.controller.panel.site;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CRUDController;
import com.fattah.dto.site.SliderDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/panel/slider")
public class SliderPanelController implements CRUDController<SliderDto> {
    private final SliderService service;

    @Autowired
    public SliderPanelController(SliderService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_slider")
    public ApiResponse<SliderDto> add(SliderDto sliderDto) {
        try {
            return ApiResponse.<SliderDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(sliderDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<SliderDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("delete_slider")
    public ApiResponse<Boolean> delete(Long id) {
        return ApiResponse.<Boolean>builder()
                .message("Deleted")
                .status(WebStatus.Success)
                .data(service.delete(id))
                .build();
    }

    @Override
    @CheckPermission("list_slider")
    public ApiPanelResponse<List<SliderDto>> getAll(Integer page, Integer size) {
        Page<SliderDto> data=service.readAll(page,size);
        return ApiPanelResponse.<List<SliderDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalCounts(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }

    @Override
    @CheckPermission("edit_slider")
    public ApiResponse<SliderDto> edit(SliderDto sliderDto) {
        try {
            return ApiResponse.<SliderDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(sliderDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<SliderDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @CheckPermission("edit_slider")
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
    @CheckPermission("edit_slider")
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
