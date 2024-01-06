package com.boardend.boardend.repository;

import com.boardend.boardend.models.FareManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareManagementRepository extends JpaRepository<FareManagement, Long> {
}

