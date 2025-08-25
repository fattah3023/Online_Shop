package com.fattah.service.payment;

import com.fattah.dto.payment.GoToPaymentDto;
import com.fattah.dto.invoice.InvoiceDto;
import com.fattah.dto.invoice.InvoiceItemDto;
import com.fattah.dto.payment.PaymentDto;
import com.fattah.dto.product.ColorDto;
import com.fattah.dto.product.ProductDto;
import com.fattah.dto.product.SizeDto;
import com.fattah.dto.user.CustomerDto;
import com.fattah.dto.user.LimitedUserDto;
import com.fattah.dto.user.UserDto;
import com.fattah.entity.order.Invoice;
import com.fattah.entity.payment.Payment;
import com.fattah.entity.payment.Transaction;
import com.fattah.entity.user.User;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.payment.PaymentRepository;
import com.fattah.repository.payment.TransactionRepository;
import com.fattah.service.order.InvoiceService;
import com.fattah.service.payment.provider.zarinpal.provider.ZarinPalProvider;
import com.fattah.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository repository;
    private final ZarinPalProvider provider;
    private final TransactionRepository transactionRepository;
    private final UserService service;
    private final InvoiceService invoiceService;
    private final ModelMapper mapper;
    private final ZarinPalProvider zarinPalProvider;

    @Autowired
    public PaymentService(PaymentRepository repository, ZarinPalProvider provider, TransactionRepository transactionRepository, UserService service, InvoiceService invoiceService, ModelMapper mapper, ZarinPalProvider zarinPalProvider) {
        this.repository = repository;
        this.provider = provider;
        this.transactionRepository = transactionRepository;
        this.service = service;
        this.invoiceService = invoiceService;
        this.mapper = mapper;
        this.zarinPalProvider = zarinPalProvider;
    }
     @Transactional
    public String goToPayment(GoToPaymentDto dto) throws ValidationException, NotFoundException {
        paymentValidation(dto);
        UserDto user = service.create(UserDto.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .mobile(dto.getMobile())
                .customer(CustomerDto.builder()
                        .firstname(dto.getFirstname())
                        .lastname(dto.getLastname())
                        .tel(dto.getTel())
                        .address(dto.getAddress())
                        .postalCode(dto.getPostalCode())
                        .build())
                .build());
        InvoiceDto invoice = invoiceService.create(InvoiceDto.builder()
                        .user(LimitedUserDto.builder().id(user.getId()).build())
                .items(dto.getItems().stream().map(x -> InvoiceItemDto.builder()
                        .product(ProductDto.builder().id(x.getProductId()).build())
                        .color(ColorDto.builder().id(x.getColorId()).build())
                        .size(SizeDto.builder().id(x.getSizeId()).build())
                        .quantity(x.getQuantity())
                        .build()).toList())
                .build());
        Payment gateway = repository.findFirstByNameEqualsIgnoreCase(dto.getGateway().toString()).orElseThrow(NotFoundException::new);
        Transaction trx = Transaction.builder()
                .amount(invoice.getTotalAmount())
                .payment(gateway)
                .description(invoice.getId()+"_"+invoice.getTotalAmount())
                .customer(mapper.map(user, User.class))
                .invoice(mapper.map(invoice, Invoice.class))
                .build();
        String result = "";
        switch (dto.getGateway()) {
            case ZarinPal -> {
                result = provider.goToPayment(trx);
            }
            case CardToCard -> {
            }
            case MellatBank -> {
            }
            case MelliBank -> {
            }
            case TejaratBank -> {
            }
            case PasargadBank -> {
            }
        }
        transactionRepository.save(trx);
        return result;
    }
    public String verify(String authority,String status) throws NotFoundException {
       if(status==null||status.isEmpty()||status.equalsIgnoreCase("NOK")){
           return "NOK";
       }
       if(status.equalsIgnoreCase("OK")){
          Transaction trx= transactionRepository.findFirstByAuthorityEqualsIgnoreCase(authority).orElseThrow(NotFoundException::new);
           Transaction verifiedTrx=zarinPalProvider.verify(trx);
           transactionRepository.save(verifiedTrx);
           return "OK";
       }
       return "NOK";
    }

    public List<PaymentDto> readAllPaymentGateways(){
        return repository.findAllByEnabledIsTrue().stream().map(x->mapper.map(x,PaymentDto.class)).toList();
    }


    // region 1-validation Check
    private static void paymentValidation(GoToPaymentDto dto) throws ValidationException {
        if (dto.getGateway() == null) {
            throw new ValidationException("لطفا درگاه پرداخت بانکی را انتخاب کنید");
        }
        if (dto.getFirstname() == null || dto.getFirstname().isEmpty()) {
            throw new ValidationException("لطفا نام خود را وارد نمایید");
        }
        if (dto.getLastname() == null || dto.getLastname().isEmpty()) {
            throw new ValidationException("لطفا نام خانوادگی خود را وارد نمایید");
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ValidationException("لطفا نام کاربری خود را وارد نمایید");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new ValidationException("لطفا رمز عبور خود را وارد نمایید");
        }
        if (dto.getMobile() == null || dto.getMobile().isEmpty()) {
            throw new ValidationException("لطفا شماره همراه خود را وارد نمایید");
        }
        if (dto.getTel() == null || dto.getTel().isEmpty()) {
            throw new ValidationException("لطفا شماره ثابت خود را وارد نمایید");
        }
        if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
            throw new ValidationException("لطفا آدرس محل سکونت خود را وارد نمایید");
        }
        if (dto.getPostalCode() == null || dto.getPostalCode().isEmpty()) {
            throw new ValidationException("لطفا کد پستی محل سکونت  خود را وارد نمایید");
        }
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ValidationException("لطفا حداقل یک محصول به سبد خرید خود اضافه کنید");
        }
    }
    //endregion
}
