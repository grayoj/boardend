package com.boardend.boardend.repository;

import com.boardend.boardend.models.MobileDelivery;
import com.boardend.boardend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMobileDeliveryRepository extends JpaRepository<MobileDelivery, Long> {
    List<MobileDelivery> findByUser(User user);
}
