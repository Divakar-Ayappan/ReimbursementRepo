package com.divum.reimbursement_platform.reimbursementRequest.dao;

import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateReimbursementRequest {

    @NotNull
    private LocalDateTime fromDate;

    @NotNull
    private LocalDateTime toDate;

    @NotNull
    private RuleCategory ruleCategory;

    @NotNull
    private Integer amount;

    @URL
    private String attachment;

    private List<LocalDateTime> requestingDates;

    @Length(max = 5, message = "Maximum 5 employees can be added")
    private List<UUID> employees;
}
