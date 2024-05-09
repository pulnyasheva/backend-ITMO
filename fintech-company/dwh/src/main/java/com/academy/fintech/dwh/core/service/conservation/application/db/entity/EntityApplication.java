package com.academy.fintech.dwh.core.service.conservation.application.db.entity;

import com.academy.fintech.dwh.core.service.conservation.application.ApplicationStatus;
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
import java.time.LocalDateTime;

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
