package com.fattah.dto.file;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String name;
    private String uuid;
    private String path;
    private String extension;
    private String contentType;
    private Long size;

}
