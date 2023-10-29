package com.assignment.diffrenz.service;

import com.assignment.diffrenz.dto.AccountDtoResponse;
import com.assignment.diffrenz.dto.StatementDtoResponse;
import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;



    public List<AccountDtoResponse> getOnAccountId(Long id) {
        return accountRepository.findById(id)
                .stream()
                .map(this::accountDTOConverter)
                .collect(Collectors.toList());

    }


    public List<AccountDtoResponse> getThreeMonthsAgo(String threeMonthsAgo) {
        return accountRepository.findThreeMonthsBack(threeMonthsAgo)
                .stream()
                .map(this::accountDTOConverter)
                .collect(Collectors.toList());
    }

    public List<AccountDtoResponse> getBetweenDates(DateRangeStatementAccountDTO dateRange) {

        List<Account> list = accountRepository.findBetweenDates(dateRange.getId(), dateRange.getFromDate(),
                dateRange.getToDate());

        return list.stream()
                .map(this::accountDTOConverter)
                .collect(Collectors.toList());
    }

    public List<AccountDtoResponse> getBetweenAmount(AmountRangeStatementAccount amountRange) {



        return accountRepository.findBetweenAmounts(amountRange.getAccountId(),
                amountRange.getFromAmount(),
                amountRange.getToAmount())
                .stream().map(this::accountDTOConverter)
                .collect(Collectors.toList());
    }
















//Convert individual Account and Statement to Dto
    private AccountDtoResponse accountDTOConverter(Account account) {

        AccountDtoResponse dtoAcc = modelMapper.map(account, AccountDtoResponse.class);
        List<StatementDtoResponse> dtoState = account.getStatements()
                .stream()
                .map(statement -> modelMapper.map(statement, StatementDtoResponse.class))
                .collect(Collectors.toList());
        dtoAcc.setStatements(dtoState);
        return dtoAcc;
    }



}
