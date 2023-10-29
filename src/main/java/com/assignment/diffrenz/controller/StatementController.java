package com.assignment.diffrenz.controller;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
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

    @PostMapping("/admin/between-dates")
    public ResponseEntity<?> getBetweenDates(@RequestBody DateRangeStatementAccountDTO dateRange) {

        try {
            return new ResponseEntity<>(statementService.getBetweenDates(dateRange), HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/admin/between-amount")
    public ResponseEntity<?> getBetweenAmounts(@RequestBody AmountRangeStatementAccount amountRange) {
        try {
            return new ResponseEntity<>(statementService.getBetweenAmount(amountRange),HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }


    @GetMapping("/user/get-all")
    public ResponseEntity<?> getAllDetails() {
        try {
            LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String threeMonthsAgoStr = threeMonthsAgo.format(dateFormatter);
            return new ResponseEntity<>(statementService.getThreeMonthsAgo(threeMonthsAgoStr), HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAllBasedOnAccId(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(statementService.getOnAccountId(id), HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }

    }


    public ResponseEntity<String> errorResponse() {
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
