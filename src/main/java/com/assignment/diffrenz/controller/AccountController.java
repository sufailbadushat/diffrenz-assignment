package com.assignment.diffrenz.controller;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import util.ValueMapper;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Validated
public class AccountController {
    @Autowired
    private AccountService statementService;

    @GetMapping
    public String welcomePage() {
        return "HELLO, WELCOME!";
    }


// Admin can access data based on account ID as path variable
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> getAllBasedOnAccId(@PathVariable Long id) throws DataNotFoundException{

        log.info("AccountController::getAllBasedOnAccId execution started for Id {}", id);
        try {
            List<AccountDtoResponse> dtoResponses = statementService.getOnAccountId(id);
            log.info("AccountController::getAllBasedOnAccId by id {} response {}", id,ValueMapper
                    .jsonAsString(dtoResponses));
            return new ResponseEntity<>(dtoResponses, HttpStatus.OK);
        } catch (Exception e) {
            log.error("AccountController::getAllBasedOnAccId, Exception occurred exception message {}", e.getMessage());
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }

    }



// Admin can access data using Account ID or ID and Date ranges
    @PostMapping("/admin/between-dates")
    public ResponseEntity<?> getBetweenDates(@Valid @RequestBody DateRangeStatementAccountDTO dateRange) {
        log.info("AccountController::getBetweenDates execution started, Request body {}",dateRange);
        List<AccountDtoResponse> dtoResponses;
        try {
            if (dateRange.getId() != null && dateRange.getToDate() == null && dateRange.getFromDate() == null) {
                dtoResponses = statementService.getOnAccountId(dateRange.getId());
                log.info("AccountController::getBetweenDates by Id {} Response {}", dateRange.getId(), dtoResponses);
            } else {
                dtoResponses = statementService.getBetweenDates(dateRange);
                log.info("AccountController::getBetweenDates, Response {}",ValueMapper.jsonAsString(dtoResponses));
            }
            return new ResponseEntity<>(dtoResponses, HttpStatus.OK);

        } catch (DataNotFoundException e) {
            log.error("AccountController::getBetweenDates Exception message: {}", e.getMessage());
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("AccountController::getBetweenDates, Exception occurred exception message: {}", e.getMessage());
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }





// Admin can access data using Account ID or ID and Amount ranges
    @PostMapping("/admin/between-amounts")
    public ResponseEntity<?> getBetweenAmounts(@Valid @RequestBody AmountRangeStatementAccount amountRange) {
        log.info("AccountController::getBetweenAmounts execution started, Request body {}",amountRange);
        List<AccountDtoResponse> dtoResponses;
        try {
            if (amountRange.getAccountId() != null && amountRange.getToAmount() == null && amountRange.getFromAmount() == null) {
                dtoResponses = statementService.getOnAccountId(amountRange.getAccountId());
                log.info("AccountController::getBetweenAmounts by Id {} Response {}", amountRange.getAccountId(), dtoResponses);
            } else {
                dtoResponses = statementService.getBetweenAmount(amountRange);
                log.info("AccountController::getBetweenAmounts, Response {}", dtoResponses);
            }
            return new ResponseEntity<>(dtoResponses, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            log.error("AccountController::getBetweenAmounts Exception message: {}", e.getMessage());
            return errorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("AccountController::getBetweenAmounts, Exception occurred exception message: {}", e.getMessage());
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }





//if user tries to pass any parameter return 401 otherwise return three months back data
    @PostMapping("/user/get-data")
    public ResponseEntity<?> userAccessData(@RequestBody(required = false) AmountRangeStatementAccount dto) {
        log.info("AccountController::userAccessData execution started, Request body {}",dto);
        try {
            if (dto != null) {
                // Request body is present, return 401 Unauthorized
                String errorMsg = "{\"error\": Unauthorized\", \"message\": \"No request body allowed\"}";
                log.info("AccountController::userAccessData, Exception occurred exception message: {}",errorMsg );
                return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
            } else {
                // Returning three months back data
                LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String threeMonthsAgoStr = threeMonthsAgo.format(dateFormatter);
                List<AccountDtoResponse> dtoResponses = statementService.getThreeMonthsAgo(threeMonthsAgoStr);

                log.info("AccountController::userAccessData, Response {}", dtoResponses);
                return new ResponseEntity<>(dtoResponses, HttpStatus.OK);
            }

        } catch (DataNotFoundException ex) {
            log.error("AccountController::userAccessData Exception message: {}", ex.getMessage());
            return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception e) {
            log.info("AccountController::userAccessData, Exception occurred exception message: {}",e.getMessage() );
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
