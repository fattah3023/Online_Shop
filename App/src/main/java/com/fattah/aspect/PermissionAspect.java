package com.fattah.aspect;

import com.fattah.annotation.CheckPermission;
import com.fattah.dto.user.PermissionDto;
import com.fattah.dto.user.UserDto;
import com.fattah.enums.WebStatus;
import com.fattah.filters.JwtFilter;
import com.fattah.model.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class PermissionAspect {
    private final HttpServletRequest httpRequest;

    @Autowired
    public PermissionAspect(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }


    @Around("@annotation(permission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, CheckPermission permission) throws Throwable {
        UserDto dto= (UserDto) httpRequest.getAttribute(JwtFilter.CURRENT_USER);
        if(dto==null){
            return ApiResponse.builder()
                    .message("please login first")
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
        List<String> permissionList = dto.getRoles().stream().flatMap(x -> x.getPermissions().stream().
                map(PermissionDto::getTitle)).toList();
        if(!permissionList.contains(permission.value())){
            return ApiResponse.builder()
                    .message("شما دسترسی ندارید")
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
        return joinPoint.proceed();
    }

}
