package com.fattah.controller.base;

import com.fattah.model.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;



public interface DeleteController<Dto> {
    @DeleteMapping("{id}")
    ApiResponse<Boolean> delete(@PathVariable Long id);
}
