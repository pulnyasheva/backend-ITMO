package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.CompositeKey;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedulePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulePaymentRepository extends JpaRepository<EntitySchedulePayment, CompositeKey> {

    List<EntitySchedulePayment> findByScheduleAndStatusInOrderByPeriodPayment(EntitySchedule schedule,
                                                                              List<PaymentStatus> statuses);
}