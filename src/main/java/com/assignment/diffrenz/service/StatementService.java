package com.assignment.diffrenz.service;

import com.assignment.diffrenz.dto.AccountDtoResponse;
import com.assignment.diffrenz.dto.StatementDtoResponse;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementService {
    @Autowired
    private AccountRepository accountRepository;


    public List<AccountDtoResponse> getOnAccountId(Long id) {
        return accountRepository.findById(id)
                .stream()
                .map(this::accountDTOConverter)
                .collect(Collectors.toList());

    }

    @Autowired
    private ModelMapper modelMapper;
    public List<AccountDtoResponse> getThreeMonthsAgo(String threeMonthsAgo) {
        return accountRepository.findThreeMonthsBack(threeMonthsAgo)
                .stream()
                .map(this::accountDTOConverter)
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
