package com.divum.reimbursement_platform.employee.entity;

import com.divum.reimbursement_platform.commons.entity.BaseTimeFields;
import com.divum.reimbursement_platform.projects.entity.Project;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends BaseTimeFields {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dob;

    private String gender;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private JobTitle jobTitle;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(mappedBy = "employees")
    private List<ReimbursementRequest> claimRequests = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}

