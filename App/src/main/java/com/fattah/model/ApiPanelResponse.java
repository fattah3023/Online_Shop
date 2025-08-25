package com.fattah.model;


import com.fattah.enums.WebStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPanelResponse<T> extends ApiResponse<T> {
    private Long totalCounts;
    private Integer totalPages;


}