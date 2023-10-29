package com.assignment.diffrenz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatementDtoResponse {
    private Long id;

    private String datefield;

    private String amount;
}
