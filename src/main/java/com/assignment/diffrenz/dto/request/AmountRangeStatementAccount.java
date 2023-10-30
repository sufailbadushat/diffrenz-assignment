package com.assignment.diffrenz.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmountRangeStatementAccount {
    @Id
    @PositiveOrZero
    @Min(1)
    private Long accountId;

    @PositiveOrZero
    private Double fromAmount;

    @PositiveOrZero
    private Double toAmount;
}
