package com.fattah.dto.invoice;

import com.fattah.dto.user.LimitedUserDto;
import com.fattah.entity.enums.OrderStatus;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
    private Long id;
    private LocalDateTime createdTime;
    private LocalDateTime payedTime;
    private OrderStatus status;
    private Long totalAmount;
    private List<InvoiceItemDto> items;
    private LimitedUserDto user;


}
