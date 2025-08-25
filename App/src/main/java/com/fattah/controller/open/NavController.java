package com.fattah.controller.open;

import com.fattah.dto.site.NavBarDto;
import com.fattah.enums.WebStatus;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.NavBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nav")
public class NavController {
    private final NavBarService service;

    @Autowired
    public NavController(NavBarService service) {
        this.service = service;
    }

    @GetMapping("")
    public ApiResponse<List<NavBarDto>> getAllNavBar(){
     return ApiResponse.<List<NavBarDto>>builder()
             .message("OK")
             .status(WebStatus.Success)
             .data(service.readAll())
             .build();
    }
}
