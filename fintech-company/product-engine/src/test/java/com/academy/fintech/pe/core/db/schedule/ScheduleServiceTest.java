package com.academy.fintech.pe.core.db.schedule;

import com.academy.fintech.pe.core.db.schedule.entity.EntityScheduleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleServiceTest {

    @Autowired
    private ScheduleRepositoryTest scheduleRepository;

    public void add(EntityScheduleTest schedule) {
        scheduleRepository.save(schedule);
    }

    public Optional<EntityScheduleTest> get(String scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }
}
