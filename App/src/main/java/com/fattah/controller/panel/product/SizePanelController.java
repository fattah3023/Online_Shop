package com.fattah.controller.panel.product;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CreateController;
import com.fattah.controller.base.ReadController;
import com.fattah.controller.base.UpdateController;
import com.fattah.dto.product.SizeDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.product.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/size")
public class SizePanelController implements CreateController<SizeDto>, ReadController<SizeDto>, UpdateController<SizeDto> {
    private final SizeService service;

    @Autowired
    public SizePanelController(SizeService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_size")
    public ApiResponse<SizeDto> add(SizeDto sizeDto) {
        try {
            return ApiResponse.<SizeDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(sizeDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<SizeDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("list_size")
    public ApiPanelResponse<List<SizeDto>> getAll(Integer page, Integer size) {
        Page<SizeDto> data=service.readAll(page,size);
        return ApiPanelResponse.<List<SizeDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalPages(data.getTotalPages())
                .totalCounts(data.getTotalElements())
                .build();
    }

    @Override
    @CheckPermission("edit_size")
    public ApiResponse<SizeDto> edit(SizeDto sizeDto) {
        try {
            return ApiResponse.<SizeDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(sizeDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<SizeDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
