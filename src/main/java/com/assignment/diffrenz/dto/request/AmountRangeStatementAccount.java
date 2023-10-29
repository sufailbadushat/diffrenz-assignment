package com.assignment.diffrenz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmountRangeStatementAccount {
    private Long accountId;
    private String fromAmount;
    private String toAmount;
}
