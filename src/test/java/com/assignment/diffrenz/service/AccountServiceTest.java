package com.assignment.diffrenz.service;

import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.dto.response.StatementDtoResponse;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.entity.Statement;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository mockAccountRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private HashService mockHashService;

    @InjectMocks
    private AccountService accountServiceUnderTest;

    @BeforeEach
    public void setUp() {
    }

    Statement STATEMENT_1 = new Statement(52L, "10-12-2020", "10");
    Statement STATEMENT_2 = new Statement(53L, "10-12-2025", "1000");
    Account ACCOUNT_1 = new Account(1L, "savings", "12345", List.of(STATEMENT_1));
    Account ACCOUNT_2 = new Account(2L, "savings", "54321", List.of(STATEMENT_2));

    StatementDtoResponse STATEMENTDTO_1 = new StatementDtoResponse(52L, "10-12-2020", "10");
    StatementDtoResponse STATEMENTDTO_2 = new StatementDtoResponse(53L, "10-12-2025", "1000");
    AccountDtoResponse ACCOUNTDTO_1 = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_1));
    AccountDtoResponse ACCOUNTDTO_2 = new AccountDtoResponse(2L, "savings", "12345", List.of(STATEMENTDTO_2));

    @Test
    void testGetOnAccountId() throws Exception {
        // Setup
        StatementDtoResponse statementDtoResponse = new StatementDtoResponse(52L, "10-12-2020", "10");
        AccountDtoResponse expectedResult = new AccountDtoResponse(1L, "savings", "12345", List.of(statementDtoResponse));

        // Configure AccountRepository.findById(1).
        Statement STATEMENT_11 = new Statement(52L, "10-12-2020", "10");
        Account ACCOUNT_11 = new Account(1L, "savings", "12345", List.of(STATEMENT_11));
        StatementDtoResponse STATEMENTDTO_11 = new StatementDtoResponse(52L, "10-12-2020", "10");
        AccountDtoResponse ACCOUNTDTO_11 = new AccountDtoResponse(1L, "savings", "12345", List.of(STATEMENTDTO_11));
        when(mockAccountRepository.findById(1L)).thenReturn(Optional.of(ACCOUNT_11));


        // Configure ModelMapper.map
        when(mockModelMapper.map(ACCOUNT_11, AccountDtoResponse.class)).thenReturn(ACCOUNTDTO_11);
        when(mockModelMapper.map(STATEMENT_11, StatementDtoResponse.class)).thenReturn(STATEMENTDTO_11);

        // Run the test
        AccountDtoResponse result = accountServiceUnderTest.getOnAccountId(1L);

        // Verify the results
        assertNotNull(result);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetOnAccountId_AccountRepositoryReturnsAbsent() {
        // Setup
        when(mockAccountRepository.findById(11L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> accountServiceUnderTest.getOnAccountId(11L)).isInstanceOf(DataNotFoundException.class);
    }


    private static List<AccountDtoResponse> getAccountDtoResponses() {
        StatementDtoResponse statementDtoResponse1 = new StatementDtoResponse(52L, "10-12-2020", "10");
        StatementDtoResponse statementDtoResponse2 = new StatementDtoResponse(53L, "10-12-2025", "1000");
        AccountDtoResponse expectedAccount1 = new AccountDtoResponse(1L, "savings", "1234567890", List.of(statementDtoResponse1));
        AccountDtoResponse expectedAccount2 = new AccountDtoResponse(2L, "savings", "0987654321", List.of(statementDtoResponse2));
        return List.of(expectedAccount1, expectedAccount2);
    }

    @Test
    void testGetThreeMonthsAgo() throws Exception {

        // Expected Data
        List<AccountDtoResponse> expectedAccountDtoResponses = getAccountDtoResponses();

        // Configure AccountRepository.findThreeMonthsBack.
        List<Account> accountsList = List.of(ACCOUNT_1, ACCOUNT_2);
        when(mockAccountRepository.findThreeMonthsBack("10-09-2020")).thenReturn(accountsList);

        List<AccountDtoResponse> accountDtoResponseList = Arrays.asList(ACCOUNTDTO_1, ACCOUNTDTO_2);

        // Configure ModelMapper.map for individual accounts
        when(mockModelMapper.map(ACCOUNT_1, AccountDtoResponse.class)).thenReturn(ACCOUNTDTO_1);
        when(mockModelMapper.map(ACCOUNT_2, AccountDtoResponse.class)).thenReturn(ACCOUNTDTO_2);

        // Configure ModelMapper.map(...) for statements
        when(mockModelMapper.map(STATEMENT_1, StatementDtoResponse.class)).thenReturn(STATEMENTDTO_1);
        when(mockModelMapper.map(STATEMENT_2, StatementDtoResponse.class)).thenReturn(STATEMENTDTO_2);

        // Configure Hashing
        when(mockHashService.hashString(anyString())).thenReturn("1234567890","0987654321");

        // Run the test
        List<AccountDtoResponse> result = accountServiceUnderTest.getThreeMonthsAgo("10-09-2020");

        // Verify the results
        assertThat(result).isEqualTo(expectedAccountDtoResponses);
    }


    @Test
    void testGetThreeMonthsAgo_AccountRepositoryReturnsNoItems() {
        // Setup
        when(mockAccountRepository.findThreeMonthsBack("23-02-2023")).thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> accountServiceUnderTest.getThreeMonthsAgo("23-02-2023"))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void testGetBetweenDates() throws Exception {
        // Setup
        StatementDtoResponse statementDtoResponse = new StatementDtoResponse(52L, "10-12-2020", "10");
        DateRangeStatementAccountDTO dateRange = new DateRangeStatementAccountDTO(1L, "10-12-2023", "01-12-2025");
        AccountDtoResponse expectedResult = new AccountDtoResponse(1L, "savings", "12345", List.of(statementDtoResponse));


        // Configure AccountRepository.findBetweenDates.
       // final Account account1 = new Account(1L, "savings", "12345", List.of(STATEMENT_1));
        final Optional<Account> account = Optional.of(ACCOUNT_1);
        when(mockAccountRepository.findBetweenDates(dateRange.getId(),
                dateRange.getFromDate(),
                dateRange.getToDate()))
                .thenReturn(account);

        // Configure ModelMapper.map(...).

        when(mockModelMapper.map(ACCOUNT_1, AccountDtoResponse.class)).thenReturn(ACCOUNTDTO_1);
        when(mockModelMapper.map(STATEMENT_1, StatementDtoResponse.class)).thenReturn(STATEMENTDTO_1);

        // Run the test
        final AccountDtoResponse result = accountServiceUnderTest.getBetweenDates(dateRange);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBetweenDates_AccountRepositoryReturnsAbsent() {
        // Setup
        final DateRangeStatementAccountDTO dateRange = new DateRangeStatementAccountDTO(1L, "10-03-2023", "12-05-2030");
        when(mockAccountRepository.findBetweenDates(1L, "10-03-2023", "12-05-2030")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> accountServiceUnderTest.getBetweenDates(dateRange))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void testGetBetweenAmount() throws Exception {
        // Setup
        AmountRangeStatementAccount amountRange = new AmountRangeStatementAccount(1L, 10.0, 100.0);

        StatementDtoResponse statementDtoResponse = new StatementDtoResponse(52L, "10-12-2020", "10");
        AccountDtoResponse expectedResult = new AccountDtoResponse(1L, "savings", "12345", List.of(statementDtoResponse));

        // Configure AccountRepository.findBetweenAmounts(...).

        when(mockAccountRepository.findBetweenAmounts(amountRange.getAccountId(),
                amountRange.getFromAmount(),
                amountRange.getToAmount()))
                .thenReturn(Optional.of(ACCOUNT_1));

        // Configure ModelMapper.map(...).

        when(mockModelMapper.map(ACCOUNT_1, AccountDtoResponse.class)).thenReturn(ACCOUNTDTO_1);
        when(mockModelMapper.map(STATEMENT_1, StatementDtoResponse.class)).thenReturn(STATEMENTDTO_1);

        // Run the test
        final AccountDtoResponse result = accountServiceUnderTest.getBetweenAmount(amountRange);

        // Verify the results
        assertNotNull(result);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetBetweenAmount_AccountRepositoryReturnsAbsent() {
        // Setup
        final AmountRangeStatementAccount amountRange = new AmountRangeStatementAccount(1L, 6.0, 700.0);
        when(mockAccountRepository.findBetweenAmounts(1L, 6.0, 700.0)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> accountServiceUnderTest.getBetweenAmount(amountRange))
                .isInstanceOf(DataNotFoundException.class);
    }
}
