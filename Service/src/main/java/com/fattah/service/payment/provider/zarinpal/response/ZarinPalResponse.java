package com.fattah.service.payment.provider.zarinpal.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalResponse {
    private String code;
    private String message;
    private String authority;
    private String fee_type;
    private Integer fee;
}
