package com.boardend.boardend.repository;

import java.util.List;

import com.boardend.boardend.models.Delivery;

import com.boardend.boardend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByDelivered(boolean delivered);

    Page<Delivery> findByPackageName(String packageName, Pageable pageable);

    Page<Delivery> findByUser(User user, String packageName, Pageable pageable);

}