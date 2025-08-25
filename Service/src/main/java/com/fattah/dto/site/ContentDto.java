package com.fattah.dto.site;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private Long id;
    private String keyName;
    private String valueContent;
}
