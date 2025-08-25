package com.fattah.service.site;

import com.fattah.base.CRUDService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.site.BlogDto;
import com.fattah.dto.site.LimitedBlogDto;
import com.fattah.dto.site.SingleBlogDto;
import com.fattah.entity.enums.BlogStatus;
import com.fattah.entity.site.Blog;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.file.FileRepository;
import com.fattah.repository.site.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService implements CRUDService<BlogDto>, ValidationCheck<BlogDto> {
    private final BlogRepository repository;
    private final FileRepository fileRepository;
    private final ModelMapper mapper;

    @Autowired
    public BlogService(BlogRepository repository, FileRepository fileRepository, ModelMapper mapper) {
        this.repository = repository;
        this.fileRepository = fileRepository;
        this.mapper = mapper;
    }
    @Override
    public Page<BlogDto> readAll(Integer page, Integer size){
        if (page==null){
            page=0;
        }
        if(size==null){
            size=16;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,BlogDto.class));
    }


    public SingleBlogDto readBlogById(Long id) throws NotFoundException {
        Blog blg= repository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.map(blg,SingleBlogDto.class);
    }


    @Override
    public BlogDto create(BlogDto blogDto) throws ValidationException, NotFoundException {
        checkValidation(blogDto);
        Blog blog=mapper.map(blogDto,Blog.class);
        if(blog.getPublishDate()==null){
            blog.setPublishDate(LocalDateTime.now());
        }
        if(blog.getStatus()==null){
            blog.setStatus(BlogStatus.Published);
        }
            blog.setVisitCount(0L);
        Blog data=repository.save(blog);
        return mapper.map(data,BlogDto.class);
    }

    @Override
    public Boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }



    @Override
    public BlogDto update(BlogDto blogDto) throws ValidationException, NotFoundException {
        checkValidation(blogDto);
        if(blogDto.getId()==null||blogDto.getId()<0){
            throw new ValidationException("invalid id number");
        }
        Blog oldData=repository.findById(blogDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setTitle(Optional.ofNullable(blogDto.getTitle()).orElse(oldData.getTitle()));
        oldData.setSubTitle(Optional.ofNullable(blogDto.getSubTitle()).orElse(oldData.getSubTitle()));
        oldData.setPublishDate(Optional.ofNullable(blogDto.getPublishDate()).orElse(oldData.getPublishDate()));
        oldData.setStatus(Optional.ofNullable(blogDto.getStatus()).orElse(oldData.getStatus()));
        oldData.setDescription(Optional.ofNullable(blogDto.getDescription()).orElse(oldData.getDescription()));
        return mapper.map(repository.save(oldData),BlogDto.class);
    }

    @Override
    public void checkValidation(BlogDto blogDto) throws ValidationException {
        if(blogDto==null){
            throw new ValidationException("please fill blog data");
        }
        if(blogDto.getTitle()==null||blogDto.getTitle().isEmpty()){
            throw new ValidationException("please enter title");
        }
        if(blogDto.getSubTitle()==null||blogDto.getSubTitle().isEmpty()){
            throw new ValidationException("please enter subtitle");
        }

    }

    public List<LimitedBlogDto> readAllBlogs(Integer page, Integer size) {
        if (page==null){
            page=0;
        }
        if(size==null){
            size=16;
        }
        return repository.findAllPublished(Pageable.ofSize(size).withPage(page)).stream().map(x->mapper.map(x,LimitedBlogDto.class)).toList();
    }



    }

