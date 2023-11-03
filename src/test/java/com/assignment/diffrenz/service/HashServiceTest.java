package com.assignment.diffrenz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashServiceTest {

    private HashService hashServiceUnderTest;

    @BeforeEach
    void setUp() {
        hashServiceUnderTest = new HashService();
    }

    @Test
    void testHashString() {
        assertThat(hashServiceUnderTest.hashString("123456")).isEqualTo("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92");
    }
}
