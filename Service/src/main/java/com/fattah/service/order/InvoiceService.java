package com.fattah.service.order;


import com.fattah.base.CreateService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.invoice.InvoiceDto;
import com.fattah.dto.product.ProductDto;
import com.fattah.dto.site.SingleBlogDto;
import com.fattah.dto.user.UserDto;
import com.fattah.entity.enums.OrderStatus;
import com.fattah.entity.order.Invoice;
import com.fattah.entity.order.InvoiceItem;
import com.fattah.entity.site.Blog;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.order.InvoiceItemRepository;
import com.fattah.repository.order.InvoiceRepository;
import com.fattah.service.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService implements CreateService<InvoiceDto>, ValidationCheck<InvoiceDto> {
    private final InvoiceRepository repository;
    private final InvoiceItemRepository itemRepository;
    private final ProductService productService;
    private final ModelMapper mapper;

    @Autowired
    public InvoiceService(InvoiceRepository repository, InvoiceItemRepository itemRepository, ProductService productService, ModelMapper mapper) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.productService = productService;
        this.mapper = mapper;
    }

@Override
    public InvoiceDto create(InvoiceDto dto) throws ValidationException, NotFoundException {
        checkValidation(dto);
       Invoice invoice= mapper.map(dto, Invoice.class);
       invoice.setCreatedTime(LocalDateTime.now());
       invoice.setPayedTime(null);
       invoice.setStatus(OrderStatus.InProgress);
       long totalAmount=0L;
       if(invoice.getItems()!=null&& !invoice.getItems().isEmpty()){
         for(InvoiceItem invItem:invoice.getItems()){
             ProductDto productDto=productService.readProductById(invItem.getProduct().getId());
             invItem.setPrice(productDto.getPrice());
             totalAmount+=productDto.getPrice()*invItem.getQuantity();
         }
       }
       invoice.setTotalAmount(totalAmount);
       Invoice saved=repository.save(invoice);
       return mapper.map(saved,InvoiceDto.class);

    }




    @Override
    public void checkValidation(InvoiceDto dto) throws ValidationException {
        if(dto.getId()==null|| dto.getId()<0){
            throw new ValidationException("شماره فاکتور اشتباه است !");
        }
        if(dto.getItems()==null|| dto.getItems().isEmpty()){
            throw new ValidationException("هیچ محصولی در فاکتور نیست !");
        }
    }
    public List<InvoiceDto> readAllById(Long userId){
      return repository.findAllByUser_Id(userId).stream().map(x->mapper.map(x, InvoiceDto.class)).toList();
    }
    public InvoiceDto read(Long id) throws NotFoundException {
        Invoice invoice= repository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.map(invoice,InvoiceDto.class);
    }
}
