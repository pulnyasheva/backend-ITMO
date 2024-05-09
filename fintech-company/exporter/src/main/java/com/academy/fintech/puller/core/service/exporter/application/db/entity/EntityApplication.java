package com.academy.fintech.puller.core.service.exporter.application.db.entity;

import com.academy.fintech.puller.core.service.exporter.application.ApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application")
public class EntityApplication {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "requested_disbursement_amount")
    private int requestedDisbursementAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
