package com.assignment.diffrenz.repository;

import com.assignment.diffrenz.dto.AccountDtoResponse;
import com.assignment.diffrenz.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    //@Query(value = "SELECT * FROM statement s JOIN account a ON s.account_id = a.id WHERE STR_TO_DATE(s.datefield, '%d-%m-%Y') <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)", nativeQuery = true)
    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.statements s " +
            "WHERE STR_TO_DATE(s.datefield, '%d-%m-%Y') <= STR_TO_DATE(:startDate, '%d-%m-%Y')")
    List<Account> findThreeMonthsBack(@Param("startDate") String startDate);


}
