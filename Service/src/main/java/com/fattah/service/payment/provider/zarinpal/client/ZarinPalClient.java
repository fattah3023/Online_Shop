package com.fattah.service.payment.provider.zarinpal.client;

import com.fattah.service.payment.provider.zarinpal.request.ZarinPalRequest;
import com.fattah.service.payment.provider.zarinpal.request.ZarinPalVerifyRequest;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalResponse;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalResponseVerifyWrapper;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalResponseWrapper;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalVerifyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ZarinPalClient {

    @Value("${app.payment-gateway.zarinpal.base-url}")
    private String baseUrl;

    public ZarinPalResponse goToPayment(ZarinPalRequest request){
        String url=baseUrl+"v4/payment/request.json";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ZarinPalRequest> requestEntity=new HttpEntity<>(request,headers);
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<ZarinPalResponseWrapper> zarinPalResponse = restTemplate.postForEntity(url, requestEntity, ZarinPalResponseWrapper.class);
        assert zarinPalResponse.getBody() != null;
        return zarinPalResponse.getBody().getData();
    }
    public ZarinPalVerifyResponse verifyPayment(ZarinPalVerifyRequest request){
        String url=baseUrl+"v4/payment/verify.json";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ZarinPalVerifyRequest> requestEntity=new HttpEntity<>(request,headers);
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<ZarinPalResponseVerifyWrapper> zarinPalResponse = restTemplate.postForEntity(url, requestEntity, ZarinPalResponseVerifyWrapper.class);
        assert zarinPalResponse.getBody() != null;
        return zarinPalResponse.getBody().getData();
    }

}
