package com.fattah.controller.base;

import com.fattah.model.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



public interface CreateController<Dto> {
    @PostMapping("add")
    ApiResponse<Dto> add(@RequestBody Dto dto);
}
