package com.fattah.service.product;

import com.fattah.base.CRUDService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.product.LimitedProductDto;
import com.fattah.dto.product.ProductCategoryDto;
import com.fattah.dto.product.ProductDto;
import com.fattah.entity.file.File;
import com.fattah.entity.product.Color;
import com.fattah.entity.product.Product;
import com.fattah.enums.ProductType;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.product.ColorRepository;
import com.fattah.repository.product.ProductCategoryRepository;
import com.fattah.repository.product.ProductRepository;
import com.fattah.repository.product.SizeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements CRUDService<ProductDto>, ValidationCheck<ProductDto> {
    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }


    public List<LimitedProductDto> read6TopProducts(ProductType productType){
        List<Product> products=new ArrayList<>();
      switch (productType){
          case MostPopular -> {
              products= productRepository.find6PopularProducts();
          }
          case MostExpensive -> {
              products= productRepository.find6ExpensiveProducts();
          }
          case Cheapest -> {
              products= productRepository.find6CheapestProducts();
          }
          case Newest -> {
              products= productRepository.find6NewestProducts();
          }
      }
      return products.stream().map(x->mapper.map(x,LimitedProductDto.class)).toList();
    }

    public ProductDto readProductById(Long id) throws NotFoundException {
        Product product= productRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.map(product,ProductDto.class);
    }


    @Override
    public ProductDto create(ProductDto productDto) throws ValidationException, NotFoundException {
        checkValidation(productDto);
        Product product=mapper.map(productDto,Product.class);
        product.setVisitCount(0L);
        product.setAddDate(LocalDateTime.now());
        product.setEnabled(true);
        product.setExisted(true);
        return mapper.map(productRepository.save(product),ProductDto.class);

    }
    @Override
    public Boolean delete(Long id) {
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<ProductDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return productRepository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,ProductDto.class));
    }

    @Override
    public ProductDto update(ProductDto productDto) throws ValidationException, NotFoundException {
        checkValidation(productDto);
        if(productDto.getId()==null||productDto.getId()<0){
            throw new ValidationException("invalid id number");
        }
        Product oldData=productRepository.findById(productDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setTitle(Optional.ofNullable(productDto.getTitle()).orElse(oldData.getTitle()));
        oldData.setDescription(Optional.ofNullable(productDto.getDescription()).orElse(oldData.getDescription()));
        oldData.setPrice(Optional.ofNullable(productDto.getPrice()).orElse(oldData.getPrice()));
        oldData.setEnabled(Optional.ofNullable(productDto.getEnabled()).orElse(oldData.getEnabled()));
        oldData.setExisted(Optional.ofNullable(productDto.getExisted()).orElse(oldData.getExisted()));
        if(oldData.getImage()==null){
            oldData.setImage(Optional.ofNullable(mapper.map(productDto.getImage(), File.class)).orElse(oldData.getImage()));
        }

        //  oldData.setColors(Optional.ofNullable(mapper.map(productDto.getColors(), Color.class)).orElse(oldData.getColors()));

         productRepository.save(oldData);
        return mapper.map(productRepository.save(oldData),ProductDto.class);

    }

    @Override
    public void checkValidation(ProductDto productDto) throws ValidationException {
        if(productDto==null){
            throw new ValidationException("please enter product data");
        }
        if(productDto.getTitle()==null||productDto.getTitle().isEmpty()){
            throw new ValidationException("please enter product title");
        }
       /* if(productDto.getDescription()==null||productDto.getDescription().isEmpty()){
            throw new ValidationException("please enter product description");
        }
        if(productDto.getColors()==null){
            throw new ValidationException("please enter product color data");
        }
        if(productDto.getSizes()==null){
            throw new ValidationException("please enter product size data");
        }
        if(productDto.getCategory()==null){
            throw new ValidationException("please enter product category data");
        }
        if (productDto.getImage()==null) {
            throw new ValidationException("please enter product image data");
        }*/
        if(productDto.getPrice()==null||productDto.getPrice()<0){
            throw new ValidationException("product price value is invalid");
        }

    }

    public Page<ProductDto> readAllByCategory(Long cid,Integer page, Integer size) throws ValidationException {
        if(cid==null){
            throw new ValidationException("please enter category id ");
        }
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return productRepository.findAllByCategory_Id(cid,Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,ProductDto.class));
    }

}
