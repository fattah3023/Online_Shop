package com.fattah.controller.open;

import com.fattah.dto.site.ContentDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService service;

    @Autowired
    public ContentController(ContentService service) {
        this.service = service;
    }

    @GetMapping("")
    public ApiResponse<List<ContentDto>> getAllContents(){
        return ApiResponse.<List<ContentDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllContents())
                .build();
    }
    @GetMapping("{key}")
    public ApiResponse<ContentDto> getContentByKey(@PathVariable String key){
        try {
            return ApiResponse.<ContentDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.readByKey(key))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<ContentDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
