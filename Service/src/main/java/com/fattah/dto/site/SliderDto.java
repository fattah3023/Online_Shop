package com.fattah.dto.site;
import com.fattah.dto.file.FileDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SliderDto {
    private Long id;
    private String title;
    private String link;
    private Integer orderNumber;
    private FileDto image;
}
