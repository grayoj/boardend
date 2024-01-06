package com.boardend.boardend.repository;

import com.boardend.boardend.models.MobileDelivery;
import com.boardend.boardend.models.PaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaymentResponseRepository extends JpaRepository<PaymentResponse, Long>, JpaSpecificationExecutor<PaymentResponse> {
    // Add any custom methods or queries here if needed
    List<PaymentResponse> findByDeliveryIn(List<MobileDelivery> deliveries);

}