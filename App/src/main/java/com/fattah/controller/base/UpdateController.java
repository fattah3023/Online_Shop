package com.fattah.controller.base;

import com.fattah.model.ApiResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UpdateController<Dto> {
    @PutMapping("edit")
    ApiResponse<Dto> edit(@RequestBody Dto dto);
}
