package com.fattah.dto.product;

import com.fattah.dto.file.FileDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDto {
    private Long id;
    private String title;
    private String description;
    private FileDto image;
}
