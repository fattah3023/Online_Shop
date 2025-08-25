package com.fattah.base;

import com.fattah.exceptions.ValidationException;

public interface ValidationCheck <Dto>{
    void checkValidation(Dto dto) throws ValidationException;
}
