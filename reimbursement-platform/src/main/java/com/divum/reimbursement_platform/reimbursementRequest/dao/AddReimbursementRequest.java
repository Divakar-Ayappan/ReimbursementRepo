package com.divum.reimbursement_platform.reimbursementRequest.dao;

import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AddReimbursementRequest {

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotNull
    private UUID employeeId;

    @NotNull
    private RuleCategory ruleCategory;

    @NotNull
    private Integer amount;

    @URL
    private String attachment;

    private List<LocalDate> requestingDates;

    @Length(max = 5, message = "Maximum 5 employees can be added")
    private List<UUID> employees;

    private String commentByRequester;
}
