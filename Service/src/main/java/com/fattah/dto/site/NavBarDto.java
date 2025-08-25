package com.fattah.dto.site;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NavBarDto {
    private Long id;
    private String title;
    private String link;
    private Integer orderNumber;



}
