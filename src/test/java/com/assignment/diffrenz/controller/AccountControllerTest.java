package com.assignment.diffrenz.controller;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.dto.response.StatementDtoResponse;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.entity.Statement;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
       AccountService accountService1 = new AccountService();
    }

    Statement STATEMENT_1 = new Statement(52L, "10-12-2020", "10");
    Statement STATEMENT_2 = new Statement(53L, "10-12-2025", "1000");
    Account ACCOUNT_1 = new Account(1L, "savings", "12345", List.of(STATEMENT_1));
    Account ACCOUNT_2 = new Account(2L, "savings", "54321", List.of(STATEMENT_2));

    StatementDtoResponse STATEMENTDTO_1 = new StatementDtoResponse(52L, "10-12-2020", "10");
    StatementDtoResponse STATEMENTDTO_2 = new StatementDtoResponse(53L, "10-12-2025", "1000");
    AccountDtoResponse ACCOUNTDTO_1 = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
    AccountDtoResponse ACCOUNTDTO_2 = new AccountDtoResponse(2L, "savings", "12345", List.of(STATEMENTDTO_2));

    /**
     * Method under test: {@link AccountController#welcomePage()}
     */
    @Test
 //   @WithMockUser
    void testWelcomePage() throws Exception {

        String response = accountController.welcomePage();
        assertEquals("HELLO, WELCOME!", response);
    }

    /**
     * Method under test: {@link AccountController#getAllBasedOnAccId(Long)}
     */
    @Test
    void testGetAllBasedOnAccId() throws DataNotFoundException {
        Long accountId = 1L;
        AccountDtoResponse expectedResponse = new AccountDtoResponse(accountId, "savings", "12345", List.of(STATEMENTDTO_1));
        when(accountService.getOnAccountId(accountId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> responseEntity = accountController.getAllBasedOnAccId(accountId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void testGetAllBasedOnAccId_AccountServiceThrowsDataNotFoundException() throws Exception {
        when(accountService.getOnAccountId(1L)).thenThrow(DataNotFoundException.class);
        // Create an instance of AccountController and inject the mock statementService
        ResponseEntity<?> response = accountController.getAllBasedOnAccId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        //assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetBetweenDates() throws Exception {
        AccountDtoResponse accountDtoResponse = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
        DateRangeStatementAccountDTO date = new DateRangeStatementAccountDTO(1L, "10-12-2022", "12-01-2026");
       // when(accountService.getOnAccountId(date.getId())).thenReturn(accountDtoResponse);

        when(accountService.getBetweenDates(date)).thenReturn(ACCOUNTDTO_1);

        ResponseEntity<?> response = accountController.getBetweenDates(date);


        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    void testGetBetweenDatesById() throws Exception {
        AccountDtoResponse accountDtoResponse = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
        DateRangeStatementAccountDTO date = new DateRangeStatementAccountDTO();
        date.setId(1L);
        when(accountService.getOnAccountId(date.getId())).thenReturn(accountDtoResponse);

        ResponseEntity<?> response = accountController.getBetweenDates(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void testGetBetweenDates_AccountServiceGetOnAccountIdThrowsDataNotFoundException() throws Exception {

        when(accountService.getOnAccountId(1L)).thenThrow(DataNotFoundException.class);

        DateRangeStatementAccountDTO date = new DateRangeStatementAccountDTO();
        date.setId(1L);

        ResponseEntity<?> response = accountController.getBetweenDates(date);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void testGetBetweenDates_AccountServiceGetBetweenDatesThrowsDataNotFoundException() throws Exception {
        // Setup
        DateRangeStatementAccountDTO date = new DateRangeStatementAccountDTO(1L, "10-12-2022", "11-01-2025");
        when(accountService.getBetweenDates(date)).thenThrow(DataNotFoundException.class);

        ResponseEntity<?> res = accountController.getBetweenDates(date);
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void testGetBetweenAmounts() throws Exception {
        AmountRangeStatementAccount amount = new AmountRangeStatementAccount(1L, 10D, 100D);
        AccountDtoResponse expected =  new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
        when(accountService.getBetweenAmount(amount)).thenReturn(ACCOUNTDTO_1);

        ResponseEntity<?> response = accountController.getBetweenAmounts(amount);

        assertEquals(expected, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBetweenAmounts_AccountServiceGetOnAccountIdThrowsDataNotFoundException() throws Exception {
        // Setup
        AmountRangeStatementAccount amountRangeStatementAccount = new AmountRangeStatementAccount();
        amountRangeStatementAccount.setAccountId(1L);
        when(accountService.getOnAccountId(amountRangeStatementAccount.getAccountId())).thenThrow(DataNotFoundException.class);

        ResponseEntity<?> response = accountController.getBetweenAmounts(amountRangeStatementAccount);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBetweenAmounts_AccountServiceGetBetweenAmountThrowsDataNotFoundException() throws Exception {
        AmountRangeStatementAccount amountRangeStatementAccount = new AmountRangeStatementAccount(1L, 20D, 100D);
        when(accountService.getBetweenAmount(amountRangeStatementAccount))
                .thenThrow(DataNotFoundException.class);

        ResponseEntity<?> response = accountController.getBetweenAmounts(amountRangeStatementAccount);


    }

    @Test
    void testUserAccessData() throws Exception {
        AccountDtoResponse expectedResponse = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
        List<AccountDtoResponse> expectedReponseList = List.of(expectedResponse);

        AmountRangeStatementAccount amount = null;

        List<AccountDtoResponse> dtoResponses = List.of(ACCOUNTDTO_1);
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String threeMonthsAgoStr = threeMonthsAgo.format(dateFormatter);
        when(accountService.getThreeMonthsAgo(threeMonthsAgoStr)).thenReturn(dtoResponses);

        ResponseEntity<?> response = accountController.userAccessData(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testHandleAccessDenied() throws Exception {

        String msg = "Access Denied: You do not have the necessary role to access this resource.";

        ResponseEntity<?> response = accountController.handleAccessDenied();

        assertEquals(msg, response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
