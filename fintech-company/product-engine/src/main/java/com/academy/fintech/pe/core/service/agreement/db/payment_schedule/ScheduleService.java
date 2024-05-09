package com.academy.fintech.pe.core.service.agreement.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.PaymentStatus;
import com.academy.fintech.pe.core.service.agreement.Schedule;
import com.academy.fintech.pe.core.service.agreement.SchedulePayment;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedule;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedulePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SchedulePaymentRepository schedulePaymentRepository;

    public String add(Schedule schedule) {
        return scheduleRepository.save(mapScheduleToEntity(schedule)).getId();
    }

    public List<Schedule> getByAgreementId(String agreementId) {
        List<Schedule> schedules = new ArrayList<>();
        for (EntitySchedule entitySchedule : scheduleRepository.findByAgreementId(agreementId)) {
            schedules.add(mapEntityToSchedule(entitySchedule));
        }
        return schedules;
    }

    /**
     * Получает {@link Schedule} последней версии со всеми {@link SchedulePayment} остортированными по возрастанию
     * периода платежа с определенными статусами
     *
     * @param agreementId id договора для которго нужно получить {@link SchedulePayment}
     * @param statuses статусы платежей, которые нужно получить
     */
    public Schedule getStatusesPaymentsOrdered(String agreementId, List<PaymentStatus> statuses) {
        EntitySchedule schedule = scheduleRepository.findFirstByAgreementIdOrderByVersionDesc(agreementId);
        List<EntitySchedulePayment> schedulePayments = schedulePaymentRepository
                .findByScheduleAndStatusInOrderByPeriodPayment(schedule, statuses);
        schedule.setSchedulePayments(schedulePayments);
        return mapEntityToSchedule(schedule);
    }

    public void saveAllSchedulePayments(Schedule schedule) {
        List<EntitySchedulePayment> schedulePayments =
                mapListSchedulePaymentToListEntity(schedule.schedulePayments(), mapScheduleToEntity(schedule));
        schedulePaymentRepository.saveAll(schedulePayments);
    }

    private Schedule mapEntityToSchedule(EntitySchedule entitySchedule) {
        return Schedule.builder()
                .id(entitySchedule.getId())
                .agreementId(entitySchedule.getAgreementId())
                .version(entitySchedule.getVersion())
                .schedulePayments(mapListEntityPaymentToListSchedule(entitySchedule.getSchedulePayments()))
                .build();
    }

    private List<SchedulePayment> mapListEntityPaymentToListSchedule(List<EntitySchedulePayment> entityPayments) {
        List<SchedulePayment> schedulePayments = new ArrayList<>();
        for (EntitySchedulePayment entitySchedulePayment : entityPayments) {
            schedulePayments.add(mapEntityToSchedulePayment(entitySchedulePayment));
        }
        return schedulePayments;
    }

    private SchedulePayment mapEntityToSchedulePayment(EntitySchedulePayment entitySchedulePayment) {
        return SchedulePayment.builder()
                .scheduleId(entitySchedulePayment.getSchedule().getId())
                .status(entitySchedulePayment.getStatus())
                .paymentDate(entitySchedulePayment.getPaymentDate())
                .periodPayment(entitySchedulePayment.getPeriodPayment())
                .interestPayment(entitySchedulePayment.getInterestPayment())
                .principalPayment(entitySchedulePayment.getPrincipalPayment())
                .periodNumber(entitySchedulePayment.getPeriodNumber())
                .build();
    }

    private EntitySchedule mapScheduleToEntity(Schedule schedule) {
        EntitySchedule entitySchedule = new EntitySchedule();
        entitySchedule.setId(schedule.id());
        entitySchedule.setAgreementId(schedule.agreementId());
        entitySchedule.setVersion(schedule.version());
        entitySchedule.setSchedulePayments(mapListSchedulePaymentToListEntity(schedule.schedulePayments(),
                entitySchedule));
        return entitySchedule;
    }

    private List<EntitySchedulePayment> mapListSchedulePaymentToListEntity(List<SchedulePayment> schedulePayments,
                                                                           EntitySchedule entitySchedule) {
        List<EntitySchedulePayment> entitySchedulePayments = new ArrayList<>();
        for (SchedulePayment schedulePayment : schedulePayments) {
            entitySchedulePayments.add(mapSchedulePaymentToEntity(schedulePayment, entitySchedule));
        }
        return entitySchedulePayments;
    }

    private EntitySchedulePayment mapSchedulePaymentToEntity(SchedulePayment schedulePayment,
                                                             EntitySchedule entitySchedule) {
        return EntitySchedulePayment.builder()
                .schedule(entitySchedule)
                .status(schedulePayment.getStatus())
                .paymentDate(schedulePayment.getPaymentDate())
                .periodPayment(schedulePayment.getPeriodPayment())
                .interestPayment(schedulePayment.getInterestPayment())
                .principalPayment(schedulePayment.getPrincipalPayment())
                .periodNumber(schedulePayment.getPeriodNumber())
                .build();
    }
}
