package com.academy.fintech.origination.core.db.application.entity;

import com.academy.fintech.origination.core.service.application.ApplicationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application")
@EqualsAndHashCode
public class EntityApplicationTest {

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
}
