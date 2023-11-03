package com.assignment.diffrenz.repository;

import com.assignment.diffrenz.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.statements s " +
            "WHERE STR_TO_DATE(s.datefield, '%d-%m-%Y') <= STR_TO_DATE(:startDate, '%d-%m-%Y')")
    List<Account> findThreeMonthsBack(@Param("startDate") String startDate);

    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.statements s WHERE a.id =:id AND " +
            "STR_TO_DATE(s.datefield, '%d-%m-%Y') >= STR_TO_DATE(:fromDate, '%d-%m-%Y') " +
            "AND STR_TO_DATE(s.datefield, '%d-%m-%Y') <= STR_TO_DATE(:toDate, '%d-%m-%Y')" )
    Optional<Account> findBetweenDates(@Param("id") Long id,
                              @Param("fromDate") String fromDate,
                              @Param("toDate") String toDate
    );


    @Query("SELECT a FROM Account a JOIN FETCH a.statements s WHERE a.id =:accountId " +
            "AND CAST(s.amount AS double) BETWEEN CAST(:fromAmount AS double) AND CAST(:toAmount AS integer)")
    Optional<Account> findBetweenAmounts(@Param("accountId") Long accountId,
                                @Param("fromAmount") Double fromAmount,
                                @Param("toAmount") Double toAmount);

}
