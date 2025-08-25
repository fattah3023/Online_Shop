package com.fattah.repository.payment;

import com.fattah.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findFirstByNameEqualsIgnoreCase(String name);

    List<Payment> findAllByEnabledIsTrue();
}
