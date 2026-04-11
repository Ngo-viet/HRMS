package com.hrms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDto {

    @NotNull(message = "employeeId is required")
    private Integer employeeId;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @FutureOrPresent(message = "endDate must be today or in the future")
    private LocalDate endDate;

    @NotBlank(message = "contractType is required")
    private String contractType;

    @NotBlank(message = "contractSalary is required")
    private String contractSalary;

    private String status;

    private String notes;
}
