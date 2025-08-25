package com.fattah.controller.base;

import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;



public interface ReadController<Dto> {
    @GetMapping("")
    ApiPanelResponse<List<Dto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size);
}
