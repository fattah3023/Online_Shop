package com.fattah.service.site;

import com.fattah.base.CRUDService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.file.FileDto;
import com.fattah.dto.site.SliderDto;
import com.fattah.entity.file.File;
import com.fattah.entity.site.Slider;
import com.fattah.entity.site.Slider;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.file.FileRepository;
import com.fattah.repository.site.SliderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SliderService implements CRUDService<SliderDto>, ValidationCheck<SliderDto> {
    private final SliderRepository repository;
    private final FileRepository fileRepository;
    private final ModelMapper mapper;

    @Autowired
    public SliderService(SliderRepository repository, FileRepository fileRepository, ModelMapper mapper) {
        this.repository = repository;
        this.fileRepository = fileRepository;
        this.mapper = mapper;
    }

    public List<SliderDto> readAllSliders(){
        return repository.findAllByEnabledIsTrueOrderByOrderNumberAsc().stream().map(x->mapper.map(x, SliderDto.class)).toList();
    }

    @Override
    public SliderDto create(SliderDto sliderDto) throws ValidationException, NotFoundException {
        checkValidation(sliderDto);
        Slider slider=mapper.map(sliderDto,Slider.class);
        Integer number= repository.findLastOrderNumber();
        if(number==null){
            number=0;
        }
        slider.setOrderNumber(++number);
        slider.setEnabled(true);
        Slider saved=repository.save(slider);
        return mapper.map(saved,SliderDto.class);

    }

    @Override
    public Boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public Page<SliderDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,SliderDto.class));
    }

    @Override
    public SliderDto update(SliderDto sliderDto) throws ValidationException, NotFoundException {
        checkValidation(sliderDto);
        if(sliderDto.getId()==null||sliderDto.getId()<0){
            throw new ValidationException("invalid id number");
        }
        Slider oldData=repository.findById(sliderDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setOrderNumber(Optional.ofNullable(sliderDto.getOrderNumber()).orElse(oldData.getOrderNumber()));
        oldData.setTitle(Optional.ofNullable(sliderDto.getTitle()).orElse(oldData.getTitle()));
        oldData.setLink(Optional.ofNullable(sliderDto.getLink()).orElse(oldData.getLink()));
        if(oldData.getImage()!=null) {
            oldData.setImage(Optional.ofNullable(mapper.map(sliderDto.getImage(), File.class)).orElse(oldData.getImage()));
        }
        return mapper.map(repository.save(oldData),SliderDto.class);
    }
    @Transactional
    public boolean swapUp(Long id) throws NotFoundException {
        Slider slider = repository.findById(id).orElseThrow(NotFoundException::new);
        Optional<Slider> previous = repository.findFirstByOrderNumberLessThanOrderByOrderNumberDesc(slider.getOrderNumber());
        if(previous.isPresent()){
            Integer temp=slider.getOrderNumber();
            slider.setOrderNumber(previous.get().getOrderNumber());
            previous.get().setOrderNumber(temp);
            repository.save(slider);
            repository.save(previous.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean swapDown(Long id) throws NotFoundException {
        Slider slider = repository.findById(id).orElseThrow(NotFoundException::new);
        Optional<Slider> next = repository.findFirstByOrderNumberGreaterThanOrderByOrderNumberAsc(slider.getOrderNumber());
        if(next.isPresent()){
            Integer temp=slider.getOrderNumber();
            slider.setOrderNumber(next.get().getOrderNumber());
            next.get().setOrderNumber(temp);
            repository.save(slider);
            repository.save(next.get());
            return true;
        }
        return false;
    }
    
    @Override
    public void checkValidation(SliderDto sliderDto) throws ValidationException {
        if(sliderDto==null){
            throw new ValidationException("please fill slider data");
        }
        if(sliderDto.getTitle()==null||sliderDto.getTitle().isEmpty()){
            throw new ValidationException("please enter title");
        }
        if(sliderDto.getLink()==null||sliderDto.getLink().isEmpty()){
            throw new ValidationException("please enter link");
        }
        if(sliderDto.getImage()==null){
            throw new ValidationException("there is no image for slider");
        }
    }
}
