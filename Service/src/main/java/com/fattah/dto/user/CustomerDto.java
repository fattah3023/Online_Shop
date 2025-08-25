package com.fattah.dto.user;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String tel;
    private String address;
    private String postalCode;
}
