package com.fattah.service.payment.provider.zarinpal.provider;

import com.fattah.entity.payment.Transaction;
import com.fattah.service.payment.provider.zarinpal.client.ZarinPalClient;
import com.fattah.service.payment.provider.zarinpal.client.ZarinPalClientMock;
import com.fattah.service.payment.provider.zarinpal.request.ZarinPalRequest;
import com.fattah.service.payment.provider.zarinpal.request.ZarinPalVerifyRequest;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalResponse;
import com.fattah.service.payment.provider.zarinpal.response.ZarinPalVerifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ZarinPalProvider {

    @Value("${app.payment-gateway.zarinpal.merchant-id}")
    private String merchant_id;

    @Value("${app.payment-gateway.zarinpal.callback-url}")
    private String callback_url;

    private final ZarinPalClientMock client;

    @Value("${app.payment-gateway.zarinpal.ipg-url}")
    private String ipgUrl;


    @Autowired
    public ZarinPalProvider(ZarinPalClientMock client) {
        this.client = client;
    }
    public String goToPayment(Transaction trx){
        ZarinPalRequest request= ZarinPalRequest.builder()
                .merchant_id(merchant_id)
                .callback_url(callback_url)
                .amount(trx.getAmount().intValue())
                .currency("IRT")
                .description(trx.getDescription())
                .metadata(ZarinPalRequest.MetaData.builder()
                .email(trx.getCustomer()!=null?trx.getCustomer().getEmail():"")
                .mobile(trx.getCustomer()!=null?trx.getCustomer().getMobile():"")
                .order_id(trx.getInvoice()!=null?trx.getInvoice().getId().toString(): "")
                 .build())
                .build();
        ZarinPalResponse zarinPalResponse = client.goToPayment(request);
        if(zarinPalResponse!=null){
            trx.setAuthority(zarinPalResponse.getAuthority());
            trx.setCode(zarinPalResponse.getCode());
            trx.setResultMessage(zarinPalResponse.getMessage());
        }
        assert zarinPalResponse != null;
        return ipgUrl+zarinPalResponse.getAuthority();
    }
    public Transaction verify(Transaction trx){
        ZarinPalVerifyRequest request= ZarinPalVerifyRequest.builder()
                .merchant_id(merchant_id)
                .amount(trx.getAmount().intValue())
                .authority(trx.getAuthority())
                .build();
        ZarinPalVerifyResponse zarinPalResponse = client.verifyPayment(request);
        if(zarinPalResponse!=null){
            trx.setVerifyCode(zarinPalResponse.getCode());
            trx.setVerifyMessage(zarinPalResponse.getMessage());
            trx.setCardHash(zarinPalResponse.getCard_hash());
            trx.setCardPan(zarinPalResponse.getCard_pan());
            trx.setRefId(zarinPalResponse.getRef_id());
        }
        return trx;
    }
}
