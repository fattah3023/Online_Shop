package com.fattah.service.payment.provider.zarinpal.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalVerifyResponse {
    private String code;
    private String message;
    private Integer ref_id;
    private String card_pan;
    private String card_hash;
    private String fee_type;
    private Integer fee;


}
