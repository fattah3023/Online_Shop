package com.fattah.controller.panel.product;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CreateController;
import com.fattah.controller.base.ReadController;
import com.fattah.controller.base.UpdateController;
import com.fattah.dto.product.ColorDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.product.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/color")
public class ColorPanelController implements CreateController<ColorDto>, ReadController<ColorDto>, UpdateController<ColorDto> {
    private final ColorService service;

    @Autowired
    public ColorPanelController(ColorService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_color")
    public ApiResponse<ColorDto> add(ColorDto colorDto) {
        try {
            return ApiResponse.<ColorDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(colorDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ColorDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("list_color")
    public ApiPanelResponse<List<ColorDto>> getAll(Integer page, Integer color) {
        Page<ColorDto> data=service.readAll(page,color);
        return ApiPanelResponse.<List<ColorDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalPages(data.getTotalPages())
                .totalCounts(data.getTotalElements())
                .build();
    }

    @Override
    @CheckPermission("edit_color")
    public ApiResponse<ColorDto> edit(ColorDto colorDto) {
        try {
            return ApiResponse.<ColorDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(colorDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<ColorDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
