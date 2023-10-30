package com.assignment.diffrenz.controller;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class StatementController {
    @Autowired
    private StatementService statementService;

    @GetMapping
    public String welcomePage() {
        return "HELLO, WELCOME!";
    }

    @PostMapping("/admin/between-dates")
    public ResponseEntity<?> getBetweenDates(@RequestBody DateRangeStatementAccountDTO dateRange) {

        try {
            return new ResponseEntity<>(statementService.getBetweenDates(dateRange), HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/admin/between-amount")
    public ResponseEntity<?> getBetweenAmounts(@RequestBody AmountRangeStatementAccount amountRange) {
        try {
            return new ResponseEntity<>(statementService.getBetweenAmount(amountRange),HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/user/get-all")
    public ResponseEntity<?> getAllDetails() {
        try {
            LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String threeMonthsAgoStr = threeMonthsAgo.format(dateFormatter);
            return new ResponseEntity<>(statementService.getThreeMonthsAgo(threeMonthsAgoStr), HttpStatus.OK);
        } catch (DataNotFoundException ex) {
            return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAllBasedOnAccId (@PathVariable Long id){
        try {
            return new ResponseEntity<>(statementService.getOnAccountId(id), HttpStatus.OK);
        } catch (DataNotFoundException ex) {
            return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }

    }


    private ResponseEntity<?> errorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(message, status);
    }
}
