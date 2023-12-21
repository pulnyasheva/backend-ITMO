package com.academy.fintech.pe.core.db.schedule;

import com.academy.fintech.pe.core.db.schedule.entity.EntityScheduleTest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepositoryTest extends JpaRepository<EntityScheduleTest, String> {
}
