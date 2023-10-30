package com.assignment.diffrenz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDtoResponse {
   private Long id;
   private String accountType;
   private String accountNumber;
   private List<StatementDtoResponse> statements;
}
