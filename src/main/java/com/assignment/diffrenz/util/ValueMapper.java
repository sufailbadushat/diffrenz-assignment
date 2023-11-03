package com.assignment.diffrenz.util;

import com.assignment.diffrenz.dto.response.AccountDtoResponse;
import com.assignment.diffrenz.dto.response.StatementDtoResponse;
import com.assignment.diffrenz.entity.Account;
import com.assignment.diffrenz.service.HashService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ValueMapper {

    public static String jsonAsString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
