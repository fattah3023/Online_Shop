package com.fattah.controller.open;

import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.model.ApiResponse;
import com.fattah.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class HomeController {
    private final PaymentService paymentService;

    @Value("${app.payment-gateway.zarinpal.callback-url}")
    private String callback_url;

    @Autowired
    public HomeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("verify")
    public ApiResponse<String> verify(@RequestParam String Authority, @RequestParam String Status){
        try {
            return ApiResponse.<String>builder()
                    .message(Status)
                    .status(WebStatus.Success)
                    .data(paymentService.verify(Authority,Status))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @GetMapping("/pg/StartPay/{Authority}")
    public ApiResponse<String> startPay(@PathVariable String Authority){
        return ApiResponse.<String>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(callback_url+"?Authority="+Authority+"&Status=OK")
                .build();
    }


    @GetMapping("")
    public ApiResponse<String> index(){

            return ApiResponse.<String>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data("Website is running fine")
                    .build();
    }

}
