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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import util.ValueMapper;

import java.util.ArrayList;
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


    public List<AccountDtoResponse> getOnAccountId(Long id) throws DataNotFoundException {
        Optional<Account> ac = accountRepository.findById(id);
        List<AccountDtoResponse> dtoResponses = new ArrayList<>();
        log.info("AccountService::getOnAccountId execution started");
            if (ac.isPresent()) {
                dtoResponses = ac.stream().map(this::accountDTOConverterForAdmin)
                        .toList();
            } else {
                log.error("AccountService::getOnAccountId data not found with id {}",id);
                throw new DataNotFoundException("Data not found with id " + id);
            }

        log.info("AccountService::getOnAccountId execution ended");
        return dtoResponses;
    }


    public List<AccountDtoResponse> getThreeMonthsAgo(String threeMonthsAgo) throws DataNotFoundException {

        log.info("AccountService::getThreeMonthsAgo execution started");
        List<Account> accounts = accountRepository.findThreeMonthsBack(threeMonthsAgo);
        if (accounts.isEmpty()) {
            throw new DataNotFoundException("No data found");
        }

        log.info("AccountService::accountDTOConverterForUser execution started");
        List<AccountDtoResponse> dtoResponses = accounts.stream()
                .map(this::accountDTOConverterForUser)
                .collect(Collectors.toList());
        log.info("AccountService::accountDTOConverterForUser execution ended");

        log.info("AccountService::getThreeMonthsAgo execution ended");
        return dtoResponses;
    }

    public List<AccountDtoResponse> getBetweenDates(DateRangeStatementAccountDTO dateRange) throws DataNotFoundException {
        log.info("AccountService::getBetweenDates execution started");
        List<Account> list = accountRepository.findBetweenDates(dateRange.getId(),
                dateRange.getFromDate(),
                dateRange.getToDate());
        if (list.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }

        log.info("AccountService::accountDTOConverterForAdmin execution started");
        List<AccountDtoResponse> dtoResponses = list.stream()
                .map(this::accountDTOConverterForAdmin)
                .collect(Collectors.toList());
        log.info("AccountService::accountDTOConverterForAdmin execution ended");

        log.info("AccountService::getBetweenDates execution ended");
        return dtoResponses;
    }

    public List<AccountDtoResponse> getBetweenAmount(AmountRangeStatementAccount amountRange) throws DataNotFoundException {
        log.info("AccountService::getBetweenAmount execution started");
        List<Account> accounts = accountRepository.findBetweenAmounts(amountRange.getAccountId(), amountRange.getFromAmount(), amountRange.getToAmount());
        if (accounts.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }

        log.info("AccountService::accountDTOConverterForAdmin execution started");
        List<AccountDtoResponse> dtoResponses = accounts.stream()
                .map(this::accountDTOConverterForAdmin)
                .collect(Collectors.toList());
        log.info("AccountService::accountDTOConverterForAdmin execution ended");
        log.info("AccountService::getBetweenAmount execution ended");
        return dtoResponses;
    }


    //Convert individual Account and Statement to Dto for admin
    private AccountDtoResponse accountDTOConverterForAdmin(Account account) {
        AccountDtoResponse dtoAcc = modelMapper.map(account, AccountDtoResponse.class);

        List<StatementDtoResponse> dtoState = account.getStatements().stream()
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

        List<StatementDtoResponse> dtoState = account.getStatements().stream().map(statement -> modelMapper.map(statement, StatementDtoResponse.class)).collect(Collectors.toList());
        dtoAcc.setStatements(dtoState);
        return dtoAcc;
    }

}
