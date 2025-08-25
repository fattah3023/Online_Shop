package com.fattah.controller.panel.user;
import com.fattah.annotation.CheckPermission;
import com.fattah.controller.base.CRUDController;
import com.fattah.dto.user.ChangePassDto;
import com.fattah.dto.user.UpdateProfileDto;
import com.fattah.dto.user.UserDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.filters.JwtFilter;
import com.fattah.model.ApiPanelResponse;
import com.fattah.model.ApiResponse;
import com.fattah.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/panel/user")
public class UserPanelController implements CRUDController<UserDto> {
    private final UserService service;

    public UserPanelController(UserService service) {
        this.service = service;
    }

    @CheckPermission("user_info")
    @GetMapping("{id}")
    public ApiResponse<UserDto> getUserById(@PathVariable Long id){
     return ApiResponse.<UserDto>builder()
             .message("OK")
             .status(WebStatus.Success)
             .data(service.read(id))
             .build();
    }

    @Override
    @CheckPermission("add_user")
    public ApiResponse<UserDto> add(UserDto userDto) {
        try {
            return ApiResponse.<UserDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.create(userDto))
                    .build();
        } catch (ValidationException e) {
            return ApiResponse.<UserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

    @Override
    @CheckPermission("delete_user")
    public ApiResponse<Boolean> delete(Long id) {
        return ApiResponse.<Boolean>builder()
                .message("Deleted")
                .status(WebStatus.Success)
                .data(service.delete(id))
                .build();

    }

    @Override
    @CheckPermission("list_user")
    public ApiPanelResponse<List<UserDto>> getAll(Integer page, Integer size) {
        Page<UserDto> data=service.readAll(page,size);
        return ApiPanelResponse.<List<UserDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(data.getContent())
                .totalCounts(data.getTotalElements())
                .totalPages(data.getTotalPages())
                .build();
    }

    @Override
    @CheckPermission("edit_user")
    public ApiResponse<UserDto> edit(UserDto userDto) {
        try {
            return ApiResponse.<UserDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.update(userDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<UserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @PutMapping("change-pass/admin")
    @CheckPermission("change_password_by_admin")
    public ApiResponse<UserDto> editPasswordByAdmin(UserDto userDto) {
        try {
            return ApiResponse.<UserDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.changePasswordByAdmin(userDto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<UserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @PutMapping("change-pass")
    @CheckPermission("change_password_by_user")
    public ApiResponse<UserDto> editPasswordByUser(ChangePassDto userDto, HttpServletRequest request) {
        UserDto user=(UserDto) request.getAttribute(JwtFilter.CURRENT_USER);
        try {
            return ApiResponse.<UserDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.changePasswordByUser(userDto,user))
                    .build();
        } catch (ValidationException|NotFoundException e) {
            return ApiResponse.<UserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @PutMapping("update-profile")
    @CheckPermission("edit_my_user")
    public ApiResponse<UserDto> editProfile(UpdateProfileDto profileDto, HttpServletRequest request) {
        UserDto user=(UserDto) request.getAttribute(JwtFilter.CURRENT_USER);
        profileDto.setId(user.getId());
        try {
            return ApiResponse.<UserDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.updateProfile(profileDto))
                    .build();
        } catch (ValidationException|NotFoundException e) {
            return ApiResponse.<UserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }

}
