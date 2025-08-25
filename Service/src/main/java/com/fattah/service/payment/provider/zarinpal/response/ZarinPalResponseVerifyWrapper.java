package com.fattah.service.payment.provider.zarinpal.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalResponseVerifyWrapper {
    private ZarinPalVerifyResponse data;
    private Object [] errors;
}
