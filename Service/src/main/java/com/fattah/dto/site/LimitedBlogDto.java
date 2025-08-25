package com.fattah.dto.site;

import com.fattah.dto.file.FileDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitedBlogDto {
    private Long id;
    private String title;
    private String subTitle;
    private LocalDateTime publishDate;
    private Long visitCount;
    private FileDto image;
}
