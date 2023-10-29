package com.assignment.diffrenz.dto;

import com.assignment.diffrenz.entity.Statement;
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
