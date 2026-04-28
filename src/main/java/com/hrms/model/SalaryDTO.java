package com.hrms.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDTO {
    private Integer salaryId;
    private Integer eid;
    private String employeeFirstName;
    private String employeeLastName;
    private String month;
    private int totalWorkingDay;
    private String basic;
    private String hra;
    private String ca;
    private String pay;
    private String deduction;
}
