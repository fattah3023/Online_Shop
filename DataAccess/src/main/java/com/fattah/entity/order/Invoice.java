package com.fattah.entity.order;

import com.fattah.entity.enums.OrderStatus;
import com.fattah.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private LocalDateTime createdTime;

   private LocalDateTime payedTime;

   private OrderStatus status;

   private Long totalAmount;

   @OneToMany(mappedBy = "invoice")
   private List<InvoiceItem> items;

   @ManyToOne
   private User user;





}
