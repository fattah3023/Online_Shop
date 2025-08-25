package com.fattah.entity.payment;

import com.fattah.entity.enums.TransactionStatus;
import com.fattah.entity.order.Invoice;
import com.fattah.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="trx")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private String authority;
    private String code;
    private String verifyCode;
    private String resultMessage;
    private String verifyMessage;
    private String cardHash;
    private String cardPan;
    private Integer refId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    private Invoice invoice;

    @ManyToOne
    private User customer;

    @ManyToOne
    private Payment payment;








}
