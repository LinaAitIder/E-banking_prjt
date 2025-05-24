package org.ebanking.dao;

import org.ebanking.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByOperationType(String operationType);
    List<AuditLog> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT al FROM AuditLog al WHERE al.operationType LIKE %:operationType% AND al.date BETWEEN :startDate AND :endDate")
    List<AuditLog> findAuditsByTypeAndDate(
            @Param("operationType") String operationType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}