package com.fattah.service.product;

import com.fattah.base.CreateService;
import com.fattah.base.ReadService;
import com.fattah.base.UpdateService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.product.ColorDto;
import com.fattah.entity.product.Color;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.product.ColorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColorService implements CreateService<ColorDto>, ReadService<ColorDto>, UpdateService<ColorDto>, ValidationCheck<ColorDto> {
    private final ColorRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ColorService(ColorRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ColorDto create(ColorDto colorDto) throws ValidationException, NotFoundException {
        checkValidation(colorDto);
       Color color= repository.save(mapper.map(colorDto, Color.class));
        return mapper.map(color,ColorDto.class);
    }

    @Override
    public Page<ColorDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x -> mapper.map(x, ColorDto.class));
    }

    @Override
    public ColorDto update(ColorDto colorDto) throws ValidationException, NotFoundException {
        checkValidation(colorDto);
        if(colorDto.getId()==null||colorDto.getId()<0){
            throw new ValidationException("invalid id number");
        }
        Color oldData=repository.findById(colorDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setHex(Optional.ofNullable(colorDto.getHex()).orElse(oldData.getHex()));
        oldData.setName(Optional.ofNullable(oldData.getName()).orElse(oldData.getName()));
        return mapper.map(repository.save(oldData),ColorDto.class);
    }

    @Override
    public void checkValidation(ColorDto colorDto) throws ValidationException {
        if(colorDto==null){
            throw new ValidationException("please fill color data");
        }
        if(colorDto.getName()==null||colorDto.getName().isEmpty()){
            throw new ValidationException("please enter color name");
        }
        if(colorDto.getHex()==null||colorDto.getHex().isEmpty()){
            throw new ValidationException("please enter hex value ");
        }
    }
}
