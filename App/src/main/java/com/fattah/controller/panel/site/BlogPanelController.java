package com.fattah.controller.panel.site;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CRUDController;
import com.fattah.dto.site.BlogDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/blog")
public class BlogPanelController implements CRUDController<BlogDto> {
    private final BlogService service;

    @Autowired
    public BlogPanelController(BlogService service) {
        this.service = service;
    }

    @Override
    @CheckPermission("add_blog")
    public ApiResponse<BlogDto> add(BlogDto blogDto) {
        try {
            return ApiResponse.<BlogDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(blogDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<BlogDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("delete_blog")
    public ApiResponse<Boolean> delete(Long id) {
        return ApiResponse.<Boolean>builder()
                .message("Deleted")
                .status(WebStatus.Success)
                .data(service.delete(id))
                .build();
    }

    @Override
    @CheckPermission("list_blog")
    public ApiPanelResponse<List<BlogDto>> getAll(Integer page, Integer size) {
        Page<BlogDto> dto=service.readAll(page,size);
        return ApiPanelResponse.<List<BlogDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(dto.getContent())
                .totalPages(dto.getTotalPages())
                .totalCounts(dto.getTotalElements())
                .build();
    }

    @Override
    @CheckPermission("edit_blog")
    public ApiResponse<BlogDto> edit(BlogDto blogDto) {
        try {
            return ApiResponse.<BlogDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(blogDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<BlogDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(null)
                    .build();
        }
    }
}
