package com.academy.fintech.pe.core.service.agreement.db.payment_schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompositeKey implements Serializable {

    private EntitySchedule schedule;
    private int periodNumber;
}
