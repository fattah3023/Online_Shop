package com.fattah.service.payment.provider.zarinpal.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZarinPalRequest {
    private String merchant_id;
    private Integer amount;
    private String currency;
    private String description;
    private String callback_url;
    private MetaData metadata;

     @Getter
     @Setter
     @Builder
    public static class MetaData{
        private String email;
        private String mobile;
        private String order_id;

    }
}
