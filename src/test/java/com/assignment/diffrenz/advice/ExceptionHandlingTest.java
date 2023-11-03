package com.assignment.diffrenz.advice;

import com.assignment.diffrenz.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlingTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    DataNotFoundException tasksNotFoundException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @InjectMocks
    ExceptionHandling applicationExceptionHandler;


    @BeforeEach
    public void setUp() {

    }

    @Test
    void testInvalidRequest_Error(){

        Map<String, String> errorMap=new HashMap<>();

        FieldError fieldError1 = new FieldError("objectName1", "field1", "Field 1 error message");
        FieldError fieldError2 = new FieldError("objectName2", "field2", "Field 2 error message");

        Mockito.when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        Map<String,String> exceptionMap=applicationExceptionHandler.handleInvalidArgument(methodArgumentNotValidException);

        errorMap.put("field1", "Field 1 error message");
        errorMap.put("field2", "Field 2 error message");
        assertEquals(errorMap, exceptionMap);

        verify(methodArgumentNotValidException, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getFieldErrors();
    }

    @Test
    void testHandleProductsNotFoundException(){
        String errorMsg="Task not found with id: 1";
        Map<String, String> errorMap=new HashMap<>();
        errorMap.put("errorMessage", errorMsg);

        Mockito.when(tasksNotFoundException.getMessage()).thenReturn(errorMsg);

        Map<String,String> exception=applicationExceptionHandler.handleProductsNotFoundException(tasksNotFoundException);
        assertEquals(errorMap, exception);
    }
}