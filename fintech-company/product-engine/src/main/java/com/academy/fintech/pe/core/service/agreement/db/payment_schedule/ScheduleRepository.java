package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<EntitySchedule, String> {
    List<EntitySchedule> findByAgreementId(String agreementId);

    EntitySchedule findFirstByAgreementIdOrderByVersionDesc(String agreementId);
}
