package com.fattah.controller.open;

import com.fattah.dto.site.LimitedBlogDto;
import com.fattah.dto.site.SingleBlogDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.model.ApiResponse;
import com.fattah.service.site.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService service;

    @Autowired
    public BlogController(BlogService service) {
        this.service = service;
    }


    @GetMapping("")
    @Cacheable(cacheNames = "apiCache30m",key = "'blog_all_'+#page+'_'+#size")
    public ApiResponse<List<LimitedBlogDto>> getAllBlogs(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size){
        return ApiResponse.<List<LimitedBlogDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllBlogs(page,size))
                .build();
    }
    @GetMapping("{id}")
    @Cacheable(cacheNames = "apiCache30m",key = "'blog_'+#id")
    public ApiResponse<SingleBlogDto> getBlogByKey(@PathVariable Long id){
        try {
            return ApiResponse.<SingleBlogDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.readBlogById(id))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<SingleBlogDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
