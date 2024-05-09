package com.academy.fintech.puller.core.service.export_task.db.entity;

import com.academy.fintech.puller.core.service.enumeration.ExportType;
import com.academy.fintech.puller.core.service.enumeration.StatusTask;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "export_tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityExportTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "export_task_sequence")
    @SequenceGenerator(name = "export_task_sequence", sequenceName = "s_export_tasks", allocationSize = 1)
    @Column(name = "id")
    private BigInteger id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExportType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusTask status;

    @Column(name = "string_key")
    private String stringKey;

    @Column(name = "number_key")
    private BigInteger numberKey;

    @Column(name = "retries_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int retriesCount;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

