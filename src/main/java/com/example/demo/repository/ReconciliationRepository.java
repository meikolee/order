package com.example.demo.repository;

import com.example.demo.log.ReconciliationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReconciliationRepository extends JpaRepository<ReconciliationLog, Long> {
    List<ReconciliationLog> findByDate(String date);
}
