package com.fattah.service.payment.provider.zarinpal.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalVerifyRequest {
    private String merchant_id;
    private Integer amount;
    private String authority;
}
