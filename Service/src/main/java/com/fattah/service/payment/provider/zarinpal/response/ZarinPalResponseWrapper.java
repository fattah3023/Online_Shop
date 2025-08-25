package com.fattah.service.payment.provider.zarinpal.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalResponseWrapper {
    private ZarinPalResponse data;
    private Object[] errors;
}
