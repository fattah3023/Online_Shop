package com.fattah.dto.payment;

import com.fattah.enums.PaymentGateway;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoToPaymentDto {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String tel;
    private String postalCode;
    private String address;
    private List<BasketItemDto> items;
    private PaymentGateway gateway;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BasketItemDto{
        private Long productId;
        private Long colorId;
        private Long sizeId;
        private Integer quantity;

    }


}
