package com.fattah.service.site;

import com.fattah.base.CreateService;
import com.fattah.base.ReadService;
import com.fattah.base.UpdateService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.site.ContentDto;
import com.fattah.entity.site.Content;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.site.ContentRepository;
import jakarta.annotation.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService implements ReadService<ContentDto>, CreateService<ContentDto>, UpdateService<ContentDto>, ValidationCheck<ContentDto> {
    private final ContentRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ContentService(ContentRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ContentDto> readAllContents(){
        return repository.findAll().stream().map(x->mapper.map(x, ContentDto.class)).toList();
    }
    public ContentDto readByKey(String name) throws NotFoundException {
        Content content=repository.findFirstByKeyNameEqualsIgnoreCase(name).orElseThrow(NotFoundException::new);
        return mapper.map(content,ContentDto.class);

    }
    @Override
    public Page<ContentDto> readAll(Integer page, Integer size){
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,ContentDto.class));
    }
    @Override
    public ContentDto create(ContentDto dto) throws ValidationException {
        checkValidation(dto);
        Content data=repository.save(mapper.map(dto,Content.class));
        return mapper.map(data,ContentDto.class);
    }

    @Override
    public ContentDto update(ContentDto contentDto) throws ValidationException, NotFoundException {
        checkValidation(contentDto);
        if(contentDto.getId()==null||contentDto.getId()<0){
            throw new ValidationException("invalid ID number");
        }
        Content oldData=repository.findById(contentDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setValueContent(Optional.ofNullable(contentDto.getValueContent()).orElse(oldData.getValueContent()));
        oldData.setKeyName(Optional.ofNullable(contentDto.getKeyName()).orElse(oldData.getKeyName()));
        return mapper.map(repository.save(oldData),ContentDto.class);
    }

    @Override
    public void checkValidation(ContentDto contentDto) throws ValidationException {
        if(contentDto==null){
            throw new ValidationException("please fill content data");
        }
        if(contentDto.getValueContent()==null||contentDto.getValueContent().isEmpty()){
            throw new ValidationException("please enter valueContent");
        }
        if(contentDto.getKeyName()==null||contentDto.getKeyName().isEmpty()){
            throw new ValidationException("please enter keyName");
        }

    }
}
