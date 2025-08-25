package com.fattah.dto.user;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
        private Long id;
        private String title;
        private String description;
}
