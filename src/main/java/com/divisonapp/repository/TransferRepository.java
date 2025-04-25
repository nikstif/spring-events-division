package com.divisonapp.repository;

import com.divisonapp.model.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findAllByEventId(Long eventId);
}
