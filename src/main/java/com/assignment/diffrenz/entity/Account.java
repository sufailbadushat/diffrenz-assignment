package com.assignment.diffrenz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountType;

    @Column(name = "account_number", unique = true)
    private String accountNumber;


    @OneToMany(targetEntity = Statement.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<Statement> statements;


}
