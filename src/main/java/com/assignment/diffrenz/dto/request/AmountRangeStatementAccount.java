package com.assignment.diffrenz.dto.request;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmountRangeStatementAccount {
    @Min(value = 1, message = "accountId must be greater than or equal to 1")
    @NotNull(message = "accountId is required")
    private Long accountId;

    @DecimalMin(value = "-1.7976931348623157E308", message = "Value must be a valid number")
    @DecimalMax(value = "1.7976931348623157E308", message = "Value must be a valid number")
    private Double fromAmount;


    @DecimalMin(value = "-1.7976931348623157E308", message = "Value must be a valid number")
    @DecimalMax(value = "1.7976931348623157E308", message = "Value must be a valid number")
    private Double toAmount;
}
