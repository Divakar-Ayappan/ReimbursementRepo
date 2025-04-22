package com.divum.reimbursement_platform.reimbursementRequest.entity;

import com.divum.reimbursement_platform.commons.entity.BaseTimeFields;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReimbursementRequest extends BaseTimeFields {

    @Id
    private UUID requestId;

    private LocalDate fromDate;

    private LocalDate toDate;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private UUID managerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleCategory ruleCategory;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime actionDate;

    @URL
    private String attachment;

    private String commentByRequester;

    private String commentByManager;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectedReason;

    @OneToMany(mappedBy = "reimbursementRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Column(nullable = false)
    private List<ClaimedDate> claimedDates;

    @ManyToMany
    @JoinTable(
            name = "employee_claim_request",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

}
