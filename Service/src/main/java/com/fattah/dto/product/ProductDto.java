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
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private Long visitCount;
    private Boolean enabled=true;
    private Boolean existed=true;
    private LocalDateTime addDate;
    private FileDto image;
    private Set<SizeDto> sizes;
    private Set<ColorDto> colors;
    private ProductCategoryDto category;

}
