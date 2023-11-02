package com.assignment.diffrenz.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRangeStatementAccountDTO {

    @Min(1)
    @NotNull(message = "id is required")
    private Long id;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "Date should be in the format dd-mm-yyyy, with dd between 01 and 31, and mm between 01 and 12.")
    private String fromDate;
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$", message = "Date should be in the format dd-mm-yyyy, with dd between 01 and 31, and mm between 01 and 12.")
    private String toDate;
}
