package com.fattah.service.payment.provider.zarinpal.client;

import com.fattah.service.payment.provider.zarinpal.request.ZarinPalRequest;
import com.fattah.service.payment.provider.zarinpal.request.ZarinPalVerifyRequest;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalResponse;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalVerifyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class ZarinPalClientMock {

    @Value("${app.payment-gateway.zarinpal.base-url}")
    private String baseUrl;

    public ZarinPalResponse goToPayment(ZarinPalRequest request){
        return ZarinPalResponse.builder()
                .authority("Mock_Data_Authority_"+new Random().nextInt(1000000,9999999))
                .code("100")
                .message("MOCK_DATA")
                .build();
    }
    public ZarinPalVerifyResponse verifyPayment(ZarinPalVerifyRequest request){
        return ZarinPalVerifyResponse.builder()
                .code("100")
                .message("Verified")
                .card_hash("235345EGEDFHH46345346754YHHFHFHERR2345343")
                .card_pan("6037997558509628")
                .ref_id(201)
                .fee_type("merchant")
                .fee(0)
                .build();
    }
}
