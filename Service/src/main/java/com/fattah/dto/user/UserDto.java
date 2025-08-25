package com.fattah.dto.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String mobile;
    private String password;
    private String email;
    private LocalDateTime registeredDate;
    private Boolean enabled=true;
    private Set<RoleDto> roles;
    private CustomerDto customer;
}
