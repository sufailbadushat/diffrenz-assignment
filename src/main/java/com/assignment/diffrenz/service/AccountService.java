package com.assignment.diffrenz.service;

import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.dto.response.StatementDtoResponse;
import com.assignment.diffrenz.dto.request.AmountRangeStatementAccount;
import com.assignment.diffrenz.dto.request.DateRangeStatementAccountDTO;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.exception.DataNotFoundException;
import com.assignment.diffrenz.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HashService hashService;


    public List<AccountDtoResponse> getOnAccountId(Long id) throws DataNotFoundException {
        Optional<Account> ac = accountRepository.findById(id);

        if (ac.isPresent()) {
            return ac.stream().map(this::accountDTOConverterForAdmin)
                    .collect(Collectors.toList());
        } else {
            throw new DataNotFoundException("Data not found with id " + id);

        }
    }


    public List<AccountDtoResponse> getThreeMonthsAgo(String threeMonthsAgo) throws DataNotFoundException {
        List<Account> accounts = accountRepository.findThreeMonthsBack(threeMonthsAgo);
        if (accounts.isEmpty()) {
            throw new DataNotFoundException("No data found");
        }
        return accounts.stream()
                .map(this::accountDTOConverterForUser)
                .collect(Collectors.toList());
    }

    public List<AccountDtoResponse> getBetweenDates(DateRangeStatementAccountDTO dateRange) throws DataNotFoundException {

        List<Account> list = accountRepository.findBetweenDates(dateRange.getId(),
                dateRange.getFromDate(),
                dateRange.getToDate());
        if (list.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }

        return list.stream()
                .map(this::accountDTOConverterForAdmin)
                .collect(Collectors.toList());
    }

    public List<AccountDtoResponse> getBetweenAmount(AmountRangeStatementAccount amountRange) throws DataNotFoundException {

        List<Account> accounts = accountRepository.findBetweenAmounts(amountRange.getAccountId(), amountRange.getFromAmount(), amountRange.getToAmount());
        if (accounts.isEmpty()) {
            throw new DataNotFoundException("No data found!");
        }
        return accounts.stream()
                .map(this::accountDTOConverterForAdmin)
                .collect(Collectors.toList());
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
