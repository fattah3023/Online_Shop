package com.fattah.controller.panel.order;

import com.fattah.annotation.CheckPermission;
import com.fattah.dto.invoice.InvoiceDto;
import com.fattah.dto.user.UserDto;
import com.fattah.enums.WebStatus;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.filters.JwtFilter;
import com.fattah.model.ApiResponse;
import com.fattah.service.order.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/invoice")
public class InvoicePanelController  {
    private final InvoiceService service;

    @Autowired
    public InvoicePanelController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping("user/{uid}")
    @CheckPermission("list_invoice")
    public ApiResponse<List<InvoiceDto>> getAllByUser(@PathVariable() Long uid) {
        return ApiResponse.<List<InvoiceDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllById(uid))
                .build();
    }
    @GetMapping("{id}")
    @CheckPermission("info_invoice")
    public ApiResponse<InvoiceDto> get(@PathVariable() Long id) {
        try {
            return ApiResponse.<InvoiceDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(service.read(id))
                    .build();
        } catch (NotFoundException e) {
            return ApiResponse.<InvoiceDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }
    @GetMapping("my-invoice")
    @CheckPermission("list_my_invoice")
    public ApiResponse<List<InvoiceDto>> getMyInvoice(HttpServletRequest request) {
        UserDto dto=(UserDto) request.getAttribute(JwtFilter.CURRENT_USER);
        return ApiResponse.<List<InvoiceDto>>builder()
                .message("OK")
                .status(WebStatus.Success)
                .data(service.readAllById(dto.getId()))
                .build();
    }
    @GetMapping("my-invoice/{id}")
    @CheckPermission("info_my_invoice")
    public ApiResponse<InvoiceDto> getMyInvoiceInfo(@PathVariable Long id, HttpServletRequest request) {
        UserDto user=(UserDto) request.getAttribute(JwtFilter.CURRENT_USER);
        try {
            InvoiceDto invoiceDto=service.read(id);
            if (!invoiceDto.getUser().getId().equals(user.getId())){
                throw new ValidationException("Access Denied to view this invoice");
            }
            return ApiResponse.<InvoiceDto>builder()
                    .message("OK")
                    .status(WebStatus.Success)
                    .data(invoiceDto)
                    .build();
        } catch (NotFoundException| ValidationException e) {
            return ApiResponse.<InvoiceDto>builder()
                    .message(e.getMessage())
                    .status(WebStatus.Failed)
                    .data(null)
                    .build();
        }
    }


}
