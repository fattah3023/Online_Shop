package com.fattah.controller.panel.site;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CreateController;
import com.fattah.controller.base.ReadController;
import com.fattah.controller.base.UpdateController;
import com.fattah.dto.site.ContentDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/content")
public class ContentPanelController implements ReadController<ContentDto>, UpdateController<ContentDto>, CreateController<ContentDto> {
    private final ContentService service;

    @Autowired
    public ContentPanelController(ContentService service) {
        this.service = service;
    }


    @Override
    @CheckPermission("list_content")
    public ApiPanelResponse<List<ContentDto>> getAll(Integer page, Integer size){
       Page<ContentDto> data= service.readAll(page,size);
        return ApiPanelResponse.<List<ContentDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalCounts(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }

    @Override
    @CheckPermission("add_content")
    public ApiResponse<ContentDto> add(ContentDto contentDto) {
        try {
            return ApiResponse.<ContentDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(contentDto))
                    .build();
        } catch (ValidationException e) {
            return ApiResponse.<ContentDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("edit_content")
    public ApiResponse<ContentDto> edit(ContentDto contentDto) {
        try {
            return ApiResponse.<ContentDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(contentDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
           return ApiResponse.<ContentDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
