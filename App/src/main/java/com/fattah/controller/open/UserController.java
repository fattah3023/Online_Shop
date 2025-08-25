package com.fattah.controller.open;

import com.fattah.dto.user.LimitedUserDto;
import com.fattah.dto.user.LoginDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiResponse;
import com.fattah.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("login")
    public ApiResponse<LimitedUserDto> login(@RequestBody LoginDto dto){
        try {
          return ApiResponse.<LimitedUserDto>builder()
                  .message("OK")
                  .status(WebStatus.Success)
                  .data(service.readUserByUsernameAndPass(dto))
                  .build();
        } catch (NotFoundException | ValidationException e) {
            return ApiResponse.<LimitedUserDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
}
