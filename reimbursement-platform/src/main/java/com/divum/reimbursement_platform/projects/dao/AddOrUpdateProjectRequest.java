package com.divum.reimbursement_platform.projects.dao;

import com.divum.reimbursement_platform.projects.entity.ProjectStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AddOrUpdateProjectRequest {

    @NotNull
    @Length(min = 3, max = 50, message = "Project's name must be between 3 and 50 characters")
    private String projectName;

    @NotNull
    @Length(min = 5, max = 200, message = "Project's description must be between 3 and 200 characters")
    private String description;

    @NotNull(message = "Project must have a manager")
    private UUID managerId;

    @NotNull
    @FutureOrPresent(message = "Project's start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "Project must contain employees")
    private List<UUID> employees;

    private ProjectStatus projectStatus;
}
