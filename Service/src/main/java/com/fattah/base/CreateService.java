package com.fattah.base;

import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;

public interface CreateService <Dto>{
    Dto create(Dto dto) throws ValidationException, NotFoundException;
}
