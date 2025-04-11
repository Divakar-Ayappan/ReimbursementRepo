package com.divum.reimbursement_platform.projects.dao;

import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class GetProjectResponse {

    private Long projectId;

    private String name;

    private String description;

    private ProjectStatus status;

    private UUID managerId;

    private List<UUID> employees;

    private LocalDate startDate;
}
