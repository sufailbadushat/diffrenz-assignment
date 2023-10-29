package com.assignment.diffrenz.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Statement")
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String datefield;

    private String amount;
}
