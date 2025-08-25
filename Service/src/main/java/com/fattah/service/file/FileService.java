package com.fattah.service.file;

import com.fattah.base.DeleteService;
import com.fattah.base.ReadService;
import com.fattah.dto.file.FileDto;
import com.fattah.entity.file.File;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.file.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService implements ReadService<FileDto>,DeleteService<FileDto> {
    private final FileRepository repository;
    private final ModelMapper mapper;

    @Value("${app.file.upload.path}")
    private String uploadPath;

    @Autowired
    public FileService(FileRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<FileDto> readAll(Integer page, Integer size) {
        if(page==null){
            page=0;
        }
        if(size==null){
            size=20;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x->mapper.map(x,FileDto.class));

    }


    @Override
    public Boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    public FileDto upload(MultipartFile file) throws IOException, ValidationException {
        if(file==null||file.isEmpty()){
            throw new ValidationException("Please enter a file first");
        }
        String myFile=file.getOriginalFilename();
        if(myFile==null||myFile.isBlank()){
            throw new ValidationException("file name is not valid");
        }
        if(!myFile.contains(".")){
            throw new ValidationException("file format is not valid");
        }
        String head = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String fileName=head+"."+extension;
        File entity=File.builder()
                .extension(extension)
                .name(head)
                .localDateTime(LocalDateTime.now())
                .path(fileName)
                .uuid(UUID.randomUUID().toString())
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();
        String filePath= uploadPath+java.io.File.separator+fileName;
        Path savedPath= Paths.get(filePath);
        java.nio.file.Files.write(savedPath,file.getBytes());
        return mapper.map(repository.save(entity),FileDto.class);
    }

    public FileDto readByName(String name) throws NotFoundException {
        File file= repository.findFirstByNameEqualsIgnoreCase(name).orElseThrow(NotFoundException::new);
         return mapper.map(file,FileDto.class);
    }
}
