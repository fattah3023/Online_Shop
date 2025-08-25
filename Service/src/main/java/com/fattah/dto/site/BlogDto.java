package com.fattah.dto.site;
import com.fattah.dto.file.FileDto;
import com.fattah.entity.enums.BlogStatus;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {
    private Long id;
    private String title;
    private String subTitle;
    private LocalDateTime publishDate;
    private Long visitCount;
    private FileDto image;
    private BlogStatus status;
    private String description;
}
