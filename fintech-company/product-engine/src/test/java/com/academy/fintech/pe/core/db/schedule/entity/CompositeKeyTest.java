package com.academy.fintech.pe.core.db.schedule.entity;

import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity.EntitySchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompositeKeyTest implements Serializable {

    private EntityScheduleTest schedule;
    private int periodNumber;
}
