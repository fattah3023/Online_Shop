package com.fattah.service.product;

import com.fattah.base.CreateService;
import com.fattah.base.ReadService;
import com.fattah.base.UpdateService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.product.SizeDto;
import com.fattah.entity.product.Size;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.product.SizeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SizeService implements CreateService<SizeDto>, ReadService<SizeDto>, UpdateService<SizeDto>, ValidationCheck<SizeDto> {
    private final SizeRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public SizeService(SizeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SizeDto create(SizeDto sizeDto) throws ValidationException, NotFoundException {
        checkValidation(sizeDto);
        Size saved=repository.save(mapper.map(sizeDto, Size.class));
        return mapper.map(saved,SizeDto.class);
    }

    @Override
    public Page<SizeDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,SizeDto.class));
    }

    @Override
    public SizeDto update(SizeDto sizeDto) throws ValidationException, NotFoundException {
        checkValidation(sizeDto);
        if(sizeDto.getId()==null||sizeDto.getId()<0){
            throw new ValidationException("invalid id number");
        }
        Size oldData=repository.findById(sizeDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setTitle(Optional.ofNullable(sizeDto.getTitle()).orElse(oldData.getTitle()));
        oldData.setDescription(Optional.ofNullable(sizeDto.getDescription()).orElse(oldData.getDescription()));
        return mapper.map(repository.save(oldData),SizeDto.class);
    }

    @Override
    public void checkValidation(SizeDto sizeDto) throws ValidationException {
        if(sizeDto==null){
            throw new ValidationException("please fill size info");
        }
        if(sizeDto.getTitle()==null||sizeDto.getTitle().isEmpty()){
            throw new ValidationException("please enter size Title");
        }
        if(sizeDto.getDescription()==null||sizeDto.getDescription().isEmpty()){
            throw new ValidationException("please enter size description");
        }
    }
}
