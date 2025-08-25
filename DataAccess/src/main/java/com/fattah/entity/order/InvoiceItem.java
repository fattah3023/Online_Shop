package com.fattah.entity.order;

import com.fattah.entity.product.Color;
import com.fattah.entity.product.Product;
import com.fattah.entity.product.Size;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
   private Invoice invoice;

   @ManyToOne
   private Product product;

   @ManyToOne
   private Size size;

   @ManyToOne
   private Color color;

   private Integer quantity;

   private Long price;




}
