package com.fattah.controller.open;

import com.fattah.dto.site.SliderDto;
import com.fattah.enums.WebStatus;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/slider")
public class SliderController {
    private final SliderService service;

    @Autowired
    public SliderController(SliderService service) {
        this.service = service;
    }
    @GetMapping("")
    public ApiResponse<List<SliderDto>> getAllSliders(){
        return ApiResponse.<List<SliderDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllSliders())
                .build();
    }
}
