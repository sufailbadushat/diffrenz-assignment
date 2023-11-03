package com.assignment.diffrenz.service;

import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.dto.response.StatementDtoResponse;
import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HashService hashService;


    public AccountDtoResponse getOnAccountId(Long id) throws DataNotFoundException {
        Optional<Account> ac = accountRepository.findById(id);
        log.info("AccountService::getOnAccountId execution started");
        if (ac.isEmpty()) {
            log.error("AccountService::getOnAccountId data not found with id {}", id);
            throw new DataNotFoundException("Data not found with id " + id);
        }
        Account account = ac.get();
        log.info("AccountService::accountDTOConverterForAdmin started");
        AccountDtoResponse dtoResponses = accountDTOConverterForAdmin(account);
        log.info("AccountService::accountDTOConverterForAdmin Ended");

        log.info("AccountService::getOnAccountId execution ended");
        return dtoResponses;
    }


    public List<AccountDtoResponse> getThreeMonthsAgo(String threeMonthsAgo) throws DataNotFoundException {

        log.info("AccountService::getThreeMonthsAgo execution started");
        List<Account> account = accountRepository.findThreeMonthsBack(threeMonthsAgo);
        if (account.isEmpty()) {
            throw new DataNotFoundException("No data found");
        }

        log.info("AccountService::accountDTOConverterForUser execution started");
        List<AccountDtoResponse> dtoResponses = account.stream()
                        .map(this::accountDTOConverterForUser)
                                .collect(Collectors.toList());
        log.info("AccountService::accountDTOConverterForUser execution ended");

        log.info("AccountService::getThreeMonthsAgo execution ended");
        return dtoResponses;
    }

    public AccountDtoResponse getBetweenDates(DateRangeStatementAccountDTO dateRange) throws DataNotFoundException {
        log.info("AccountService::getBetweenDates execution started");
        Optional<Account> accountOptional = accountRepository.findBetweenDates(dateRange.getId(),
                dateRange.getFromDate(),
                dateRange.getToDate());
        if (accountOptional.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }

        log.info("AccountService::accountDTOConverterForAdmin execution started");
        Account account = accountOptional.get();
        AccountDtoResponse dtoResponse = accountDTOConverterForAdmin(account);
        log.info("AccountService::accountDTOConverterForAdmin execution ended");

        log.info("AccountService::getBetweenDates execution ended");
        return dtoResponse;
    }

    public AccountDtoResponse getBetweenAmount(AmountRangeStatementAccount amountRange) throws DataNotFoundException {
        log.info("AccountService::getBetweenAmount execution started");
        Optional<Account> accountOptional = accountRepository.findBetweenAmounts(amountRange.getAccountId(),
                amountRange.getFromAmount(),
                amountRange.getToAmount());
        if (accountOptional.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }

        log.info("AccountService::accountDTOConverterForAdmin execution started");
        Account account = accountOptional.get();
        AccountDtoResponse dtoResponses = accountDTOConverterForAdmin(account);
        log.info("AccountService::accountDTOConverterForAdmin execution ended");

        log.info("AccountService::getBetweenAmount execution ended");
        return dtoResponses;
    }


    //Convert individual Account and Statement to Dto for admin
    AccountDtoResponse accountDTOConverterForAdmin(Account account) {
        AccountDtoResponse dtoAcc = modelMapper.map(account, AccountDtoResponse.class);

        List<StatementDtoResponse> dtoState = account.getStatements()
                .stream()
                .map(statement ->
                        modelMapper.map(statement, StatementDtoResponse.class))
                .collect(Collectors.toList());
        dtoAcc.setStatements(dtoState);
        return dtoAcc;
    }


    //Convert individual Account and Statement to Dto for user
    private AccountDtoResponse accountDTOConverterForUser(Account account) {

        AccountDtoResponse dtoAcc = modelMapper.map(account, AccountDtoResponse.class);
        //Hashing the account number
        dtoAcc.setAccountNumber(hashService.hashString(dtoAcc.getAccountNumber()));

        List<StatementDtoResponse> dtoState = account.getStatements()
                .stream()
                .map(statement -> modelMapper
                        .map(statement, StatementDtoResponse.class))
                .collect(Collectors.toList());
        dtoAcc.setStatements(dtoState);
        return dtoAcc;
    }

}
