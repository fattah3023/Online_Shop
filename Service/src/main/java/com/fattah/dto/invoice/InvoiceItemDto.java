package com.fattah.dto.invoice;

import com.fattah.dto.product.ColorDto;
import com.fattah.dto.product.ProductDto;
import com.fattah.dto.product.SizeDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDto {
    private Long id;
    private ProductDto product;
    private SizeDto size;
    private ColorDto color;
    private Integer quantity;
    private Long price;

}
