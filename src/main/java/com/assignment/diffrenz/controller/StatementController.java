package com.assignment.diffrenz.controller;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.service.StatementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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


// Admin can access data based on account ID as path variable
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAllBasedOnAccId(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(statementService.getOnAccountId(id), HttpStatus.OK);
        } catch (DataNotFoundException ex) {
            return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }

    }



// Admin can access data using Account ID or ID and Date ranges
    @PostMapping("/admin/between-dates")
    public ResponseEntity<?> getBetweenDates(@Valid @RequestBody DateRangeStatementAccountDTO dateRange) {

        try {
            if (dateRange.getId() != null && dateRange.getToDate() == null && dateRange.getFromDate() == null) {
                return new ResponseEntity<>(statementService.getOnAccountId(dateRange.getId()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(statementService.getBetweenDates(dateRange), HttpStatus.OK);
            }

        } catch (DataNotFoundException e) {
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }





// Admin can access data using Account ID or ID and Amount ranges
    @PostMapping("/admin/between-amounts")
    public ResponseEntity<?> getBetweenAmounts(@Valid @RequestBody AmountRangeStatementAccount amountRange) {
        try {
            if (amountRange.getAccountId() != null && amountRange.getToAmount() == null && amountRange.getFromAmount() == null) {
                return new ResponseEntity<>(statementService.getOnAccountId(amountRange.getAccountId()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(statementService.getBetweenAmount(amountRange), HttpStatus.OK);
            }
        } catch (DataNotFoundException e) {
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }





//if user tries to pass any parameter return 401 otherwise return three months back data
    @PostMapping("/user/get-data")
    public ResponseEntity<?> createResource(@RequestBody(required = false) AmountRangeStatementAccount dto) throws DataNotFoundException {
        try {
            if (dto != null) {
                // Request body is present, return 401 Unauthorized
                return new ResponseEntity<>("{\"error\": Unauthorized\", \"message\": \"No request body allowed\"}", HttpStatus.UNAUTHORIZED);
            } else {
                // Returning three months back data
                LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String threeMonthsAgoStr = threeMonthsAgo.format(dateFormatter);
                return new ResponseEntity<>(statementService.getThreeMonthsAgo(threeMonthsAgoStr), HttpStatus.OK);
            }

        } catch (DataNotFoundException ex) {
            return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
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

// This method will invoke if any unauthorized
    @GetMapping("/access-denied")
    public ResponseEntity<?> handleAccessDenied() {
        return new ResponseEntity<>("Access Denied: You do not have the necessary role to access this resource.",
                HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<?> errorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(message, status);
    }


}
