package com.fattah.service.product;

import com.fattah.base.CreateService;
import com.fattah.base.ReadService;
import com.fattah.base.UpdateService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.product.ProductCategoryDto;
import com.fattah.entity.file.File;
import com.fattah.entity.product.ProductCategory;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.product.ProductCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService implements CreateService<ProductCategoryDto>, ReadService<ProductCategoryDto>, UpdateService<ProductCategoryDto>, ValidationCheck<ProductCategoryDto> {
    private final ProductCategoryRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductCategoryDto create(ProductCategoryDto productCategoryDto) throws ValidationException, NotFoundException {
        checkValidation(productCategoryDto);
       ProductCategory saved= repository.save(mapper.map(productCategoryDto, ProductCategory.class));
        return mapper.map(saved,ProductCategoryDto.class);
    }

    @Override
    public Page<ProductCategoryDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,ProductCategoryDto.class));
    }

    public List<ProductCategoryDto> readAllCategories(){
        return repository.findAllByEnabledIsTrue().stream().map(x->mapper.map(x,ProductCategoryDto.class)).toList();
    }

    @Override
    public ProductCategoryDto update(ProductCategoryDto productCategoryDto) throws ValidationException, NotFoundException {
        checkValidation(productCategoryDto);
        if(productCategoryDto.getId()==null||productCategoryDto.getId()<0){
            throw new ValidationException("invalid Id number");
        }
        ProductCategory oldData=repository.findById(productCategoryDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setTitle(Optional.ofNullable(productCategoryDto.getTitle()).orElse(oldData.getTitle()));
        oldData.setDescription(Optional.ofNullable(productCategoryDto.getDescription()).orElse(oldData.getDescription()));
        if(productCategoryDto.getImage()!=null) {
            oldData.setImage(Optional.ofNullable(mapper.map(productCategoryDto.getImage(), File.class)).orElse(oldData.getImage()));
        }
        return mapper.map(repository.save(oldData),ProductCategoryDto.class);

    }

    @Override
    public void checkValidation(ProductCategoryDto productCategoryDto) throws ValidationException {
        if(productCategoryDto==null){
            throw new ValidationException("please fill product category data");
        }
        if(productCategoryDto.getTitle()==null||productCategoryDto.getTitle().isEmpty()){
            throw new ValidationException("please enter category title");
        }
        if(productCategoryDto.getDescription()==null||productCategoryDto.getDescription().isEmpty()){
            throw new ValidationException("please enter category description");
        }
        if(productCategoryDto.getImage()==null){
            throw new ValidationException("there is no image for category");
        }

    }
}
