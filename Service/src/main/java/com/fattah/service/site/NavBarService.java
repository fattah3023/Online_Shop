package com.fattah.service.site;


import com.fattah.base.CRUDService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.site.NavBarDto;
import com.fattah.entity.site.NavBar;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.site.NavBarRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NavBarService implements CRUDService<NavBarDto>, ValidationCheck<NavBarDto> {
    private final NavBarRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public NavBarService(NavBarRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public List<NavBarDto> readAll(){
       return repository.findAllByEnabledIsTrueOrderByOrderNumberAsc().stream().map(x->mapper.map(x, NavBarDto.class)).toList();
    }
    @Override
    public Page<NavBarDto> readAll(Integer page, Integer size){
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x, NavBarDto.class));
    }


    @Override
    public NavBarDto create(NavBarDto navBarDto) throws ValidationException, NotFoundException {
       checkValidation(navBarDto);
        NavBar navBar=mapper.map(navBarDto,NavBar.class);
        navBar.setEnabled(true);
        Integer lastNumber=repository.findLastOrderNumber();
        if(lastNumber==null){
            lastNumber=0;
        }

        navBar.setOrderNumber(++lastNumber);
       NavBar bar= repository.save(navBar);
       return mapper.map(bar,NavBarDto.class);
    }

    @Override
    public Boolean delete(Long id) {
     repository.deleteById(id);
     return true; // اگر تونست پاک کنه true  میده.
    }

    @Override
    public NavBarDto update(NavBarDto navBarDto) throws ValidationException, NotFoundException {
        checkValidation(navBarDto);
        if(navBarDto.getId()==null||navBarDto.getId()<=0){
            throw new ValidationException("invalid id number");
        }
     NavBar oldData=repository.findById(navBarDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setOrderNumber(Optional.ofNullable(navBarDto.getOrderNumber()).orElse(oldData.getOrderNumber()));
        oldData.setLink(Optional.ofNullable(navBarDto.getLink()).orElse(oldData.getLink()));
        oldData.setTitle(Optional.ofNullable(navBarDto.getTitle()).orElse(oldData.getTitle()));
        NavBar savedNav=repository.save(oldData);
        return mapper.map(savedNav,NavBarDto.class);

    }
    @Transactional
    public boolean swapUp(Long id) throws NotFoundException {
        NavBar navBar = repository.findById(id).orElseThrow(NotFoundException::new);
        Optional<NavBar> previous = repository.findFirstByOrderNumberLessThanOrderByOrderNumberDesc(navBar.getOrderNumber());
       if(previous.isPresent()){
         Integer temp=navBar.getOrderNumber();
         navBar.setOrderNumber(previous.get().getOrderNumber());
         previous.get().setOrderNumber(temp);
         repository.save(navBar);
         repository.save(previous.get());
         return true;
       }
         return false;
    }

    @Transactional
    public boolean swapDown(Long id) throws NotFoundException {
        NavBar navBar = repository.findById(id).orElseThrow(NotFoundException::new);
        Optional<NavBar> next = repository.findFirstByOrderNumberGreaterThanOrderByOrderNumberAsc(navBar.getOrderNumber());
        if(next.isPresent()){
            Integer temp=navBar.getOrderNumber();
            navBar.setOrderNumber(next.get().getOrderNumber());
            next.get().setOrderNumber(temp);
            repository.save(navBar);
            repository.save(next.get());
            return true;
        }
        return false;
    }

    @Override
    public void checkValidation(NavBarDto navBarDto) throws ValidationException {
        if(navBarDto==null){
            throw new ValidationException("please fill nav data");
        }
        if(navBarDto.getTitle()==null||navBarDto.getTitle().isEmpty()){
            throw new ValidationException("please enter nav title");
        }
        if(navBarDto.getLink()==null||navBarDto.getLink().isEmpty()){
            throw new ValidationException("please enter nav link");
        }
    }
}
