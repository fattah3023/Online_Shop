package com.fattah.dto.product;

import com.fattah.dto.file.FileDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitedProductDto {
    private Long id;
    private String title;
    private Long price;
    private Long visitCount;
    private LocalDateTime addDate;
    private FileDto image;
    private Set<SizeDto> sizes;
    private Set<ColorDto> colors;
    private ProductCategoryDto category;

}
