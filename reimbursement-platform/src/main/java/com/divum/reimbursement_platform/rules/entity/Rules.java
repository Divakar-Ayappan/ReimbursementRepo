package com.divum.reimbursement_platform.rules.entity;

import com.divum.reimbursement_platform.commons.entity.BaseTimeFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Rules extends BaseTimeFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleCategory category;

    @Column(nullable = false)
    private Integer reimbursementLimit;

    private Integer autoApprovalLimit;

    //TODO P1
    //private JobTitle applicableFor;
    //private Boolean isGlobalRule;

    @Column(nullable = false)
    private boolean isActive = true;

    private String ruleDescription;
}
