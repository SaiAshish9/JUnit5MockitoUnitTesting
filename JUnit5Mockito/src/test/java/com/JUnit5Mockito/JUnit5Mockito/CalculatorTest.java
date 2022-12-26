package com.JUnit5Mockito.JUnit5Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    Calculator calculator;

    @BeforeEach
    void setUp(){
        calculator = new Calculator();
    }

    @Test
    public void testMultiply(){
        assertEquals(20,calculator.multiply(4,5));
    }

}
