package com.assignment.diffrenz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRangeStatementAccountDTO {

    private Long id;
    private String fromDate;
    private String toDate;
}
