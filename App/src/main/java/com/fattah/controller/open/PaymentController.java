package com.fattah.controller.open;

import com.fattah.dto.payment.GoToPaymentDto;
import com.fattah.dto.payment.PaymentDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.model.ApiResponse;
import com.fattah.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Transactional
    @PostMapping("goToPayment")
    public ApiResponse<String> goToPayment(@RequestBody GoToPaymentDto dto){
        try {
            return ApiResponse.<String>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.goToPayment(dto))
                    .build();
        } catch (ValidationException | NotFoundException e) {
            return ApiResponse.<String>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @GetMapping("gateways")
    public ApiResponse<List<PaymentDto>> getAllPaymentGateways(){
        return ApiResponse.<List<PaymentDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllPaymentGateways())
                .build();
    }
}
