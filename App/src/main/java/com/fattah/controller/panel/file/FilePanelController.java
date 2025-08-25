package com.fattah.controller.panel.file;

import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.DeleteController;
import com.fattah.controller.base.ReadController;
import com.fattah.dto.file.FileDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/panel/file")
public class FilePanelController implements ReadController<FileDto>, DeleteController<FileDto> {
    private final FileService service;

    @Autowired
    public FilePanelController(FileService service) {
        this.service = service;
    }

    @PostMapping("upload")
    @CheckPermission("add_file")
    public ApiResponse<FileDto> upload(@RequestParam("file") MultipartFile file) {

        try {
            return ApiResponse.<FileDto>builder()
                    .status(WebStatus.Success)
                    .message("OK")
                    .data(service.upload(file))
                    .build();
        } catch (IOException | ValidationException e) {
            return ApiResponse.<FileDto>builder()
                    .status(WebStatus.Failed)
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @CheckPermission("delete_file")
    @Override
    public ApiResponse<Boolean> delete(Long id) {
        return ApiResponse.<Boolean>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.delete(id))
                .build();
    }


    @CheckPermission("list_file")
    @Override
    public ApiPanelResponse<List<FileDto>> getAll(Integer page, Integer size) {
        Page<FileDto> data = service.readAll(page, size);
        return ApiPanelResponse.<List<FileDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalCounts(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();

    }


}
