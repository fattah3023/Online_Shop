package com.fattah.base;

import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;

import java.util.List;

public interface UpdateService<Dto> {
    Dto update(Dto dto) throws ValidationException, NotFoundException;
}
