package com.fattah.service.user;

import com.fattah.base.CRUDService;
import com.fattah.base.ValidationCheck;
import com.fattah.dto.user.*;
import com.fattah.entity.user.Customer;
import com.fattah.entity.user.Role;
import com.fattah.entity.user.User;
import com.fattah.exceptions.NotFoundException;
import com.fattah.exceptions.ValidationException;
import com.fattah.repository.user.CustomerRepository;
import com.fattah.repository.user.RoleRepository;
import com.fattah.repository.user.UserRepository;
import com.fattah.util.JwtUtil;
import com.fattah.utils.HashUtil;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService implements CRUDService<UserDto>, ValidationCheck<UserDto> {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository repository, RoleRepository roleRepository, CustomerRepository customerRepository, ModelMapper mapper, JwtUtil jwtUtil) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    public LimitedUserDto readUserByUsernameAndPass(LoginDto dto) throws NotFoundException, ValidationException {
        String hash = HashUtil.sha1Hash(dto.getPassword());
        User user = repository.findFirstByUsernameIgnoreCaseAndPassword
                (dto.getUsername(), hash).orElseThrow(NotFoundException::new);
        if (!user.getEnabled()) {
            throw new ValidationException("please select ");
        }
        LimitedUserDto limitedUserDto = mapper.map(user, LimitedUserDto.class);
        limitedUserDto.setToken(jwtUtil.generateToken(limitedUserDto.getUsername()));
        return limitedUserDto;
    }

    @SneakyThrows
    public UserDto readUserByUsername(String username) {
        User user = repository.findFirstByUsername(username).orElseThrow(NotFoundException::new);
        return mapper.map(user, UserDto.class);
    }

    @SneakyThrows
    public UserDto read(Long id) {
        User user = repository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto create(UserDto dto) throws ValidationException {
        checkValidation(dto);
        Optional<User> oldUser = repository.findFirstByUsername(dto.getUsername());
        if (oldUser.isPresent()) {
            throw new ValidationException("کاربری با این نام کاربری قبلا ثبت نام کرده است لطفا وارد شوید");
        }
        Customer customer = mapper.map(dto.getCustomer(), Customer.class);
        customerRepository.save(customer);
        User user = mapper.map(dto, User.class);
        user.setCustomer(customer);
        user.setPassword(HashUtil.sha1Hash(user.getPassword()));
        user.setRegisteredDate(LocalDateTime.now());
        user.setEnabled(true);
        Optional<Role> userRole = roleRepository.findFirstByNameEqualsIgnoreCase("user");
        if (userRole.isPresent()) {
            HashSet<Role> roles = new HashSet<>();
            roles.add(userRole.get());
            user.setRoles(roles);
        }
        User savedUser = repository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }


    @Override
    public Boolean delete(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public Page<UserDto> readAll(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        return repository.findAll(Pageable.ofSize(size).withPage(page)).map(x -> mapper.map(x, UserDto.class));
    }

    @Override
    public UserDto update(UserDto userDto) throws ValidationException, NotFoundException {
        checkValidation(userDto);
        if (userDto.getId() == null || userDto.getId() < 0) {
            throw new ValidationException("invalid id number");
        }
        User oldData = repository.findById(userDto.getId()).orElseThrow(NotFoundException::new);
        oldData.setEnabled(Optional.ofNullable(userDto.getEnabled()).orElse(oldData.getEnabled()));
        oldData.setEmail(Optional.ofNullable(userDto.getEmail()).orElse(oldData.getEmail()));
        oldData.setMobile(Optional.ofNullable(userDto.getMobile()).orElse(oldData.getMobile()));
        if (userDto.getCustomer() != null) {
            oldData.setCustomer(Optional.ofNullable(mapper.map(userDto.getCustomer(), Customer.class)).orElse(oldData.getCustomer()));
        }
        return mapper.map(repository.save(oldData), UserDto.class);
    }

    public UserDto changePasswordByAdmin(UserDto userDto) throws ValidationException, NotFoundException {

        if (userDto.getId() == null || userDto.getId() < 0) {
            throw new ValidationException("invalid id number");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new ValidationException("current password cannot be null");
        }

        User oldData = repository.findById(userDto.getId()).orElseThrow(NotFoundException::new);
        String password = HashUtil.sha1Hash(userDto.getPassword());
        oldData.setPassword(password);
        return mapper.map(repository.save(oldData), UserDto.class);
    }

    public UserDto changePasswordByUser(ChangePassDto changePassDto, UserDto userDto) throws ValidationException, NotFoundException {
        if (changePassDto == null) {
            throw new ValidationException("please enter user info");
        }
        if (changePassDto.getOldPassword() == null || changePassDto.getOldPassword().isEmpty()) {
            throw new ValidationException("please enter old password");
        }
        if (changePassDto.getNewPassword() == null || changePassDto.getNewPassword().isEmpty()) {
            throw new ValidationException("please enter new password");
        }
        if (changePassDto.getNewPassword2() == null || changePassDto.getNewPassword2().isEmpty()) {
            throw new ValidationException("please enter new password again");
        }
        if (!changePassDto.getNewPassword().equals(changePassDto.getNewPassword2())) {
            throw new ValidationException(" passwords do not match ");
        }
        User user = repository.findById(userDto.getId()).orElseThrow(NotFoundException::new);
        if (!user.getPassword().equals(HashUtil.sha1Hash(changePassDto.getOldPassword()))) {
            throw new ValidationException("Incorrect old password");
        }
        user.setPassword(HashUtil.sha1Hash(changePassDto.getNewPassword()));
        repository.save(user);
        return userDto;

    }



    public UserDto updateProfile(UpdateProfileDto profileDto) throws ValidationException, NotFoundException {
        profileValidation(profileDto);
        if (profileDto.getId() == null || profileDto.getId() < 0) {
            throw new ValidationException("invalid id number");
        }
        User oldData = repository.findById(profileDto.getId()).orElseThrow(NotFoundException::new);
        oldData.getCustomer().setFirstname(Optional.ofNullable(profileDto.getFirstname()).orElse(oldData.getCustomer().getFirstname()));
        oldData.getCustomer().setLastname(Optional.ofNullable(profileDto.getLastname()).orElse(oldData.getCustomer().getLastname()));
        oldData.setEmail(Optional.ofNullable(profileDto.getEmail()).orElse(oldData.getEmail()));
        oldData.setMobile(Optional.ofNullable(profileDto.getMobile()).orElse(oldData.getMobile()));
        oldData.getCustomer().setAddress(Optional.ofNullable(profileDto.getAddress()).orElse(oldData.getCustomer().getAddress()));
        oldData.getCustomer().setTel(Optional.ofNullable(profileDto.getTel()).orElse(oldData.getCustomer().getTel()));
        oldData.getCustomer().setPostalCode(Optional.ofNullable(profileDto.getPostalCode()).orElse(oldData.getCustomer().getPostalCode()));
        return mapper.map(repository.save(oldData), UserDto.class);
    }

    private void profileValidation(UpdateProfileDto profileDto) throws ValidationException {
        if (profileDto==null){
            throw new ValidationException("please fill data first");
        }
        if(profileDto.getFirstname()==null||profileDto.getFirstname().isEmpty()){
            throw new ValidationException("please enter firstname");
        }
        if(profileDto.getLastname()==null||profileDto.getLastname().isEmpty()){
            throw new ValidationException("please enter lastname");
        }
        if(profileDto.getEmail()==null||profileDto.getEmail().isEmpty()){
            throw new ValidationException("please enter email");
        }
        if(profileDto.getMobile()==null||profileDto.getMobile().isEmpty()){
            throw new ValidationException("please enter mobile number");
        }
        if(profileDto.getAddress()==null||profileDto.getAddress().isEmpty()){
            throw new ValidationException("please enter address first");
        }
        if(profileDto.getTel()==null||profileDto.getTel().isEmpty()){
            throw new ValidationException("please enter Tel number");
        }

        if(profileDto.getPostalCode()==null||profileDto.getPostalCode().isEmpty()){
            throw new ValidationException("please enter postalCode");
        }
    }
    @Override
    public void checkValidation(UserDto dto) throws ValidationException {
        if (dto.getCustomer() == null) {
            throw new ValidationException("اطلاعات مشتری را وارد کنید");
        }
        if (dto.getCustomer().getAddress() == null || dto.getCustomer().getAddress().isEmpty()) {
            throw new ValidationException("لطفا آدرس را وارد کنید");
        }
        if (dto.getMobile() == null || dto.getMobile().isEmpty()) {
            throw new ValidationException("لطفا شماره موبایل را وارد کنید");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new ValidationException("لطفا ایمیل خود را وارد کنید");
        }
        if (dto.getCustomer().getFirstname() == null || dto.getCustomer().getFirstname().isEmpty()) {
            throw new ValidationException("لطفا نام خود را وارد کنید");
        }
        if (dto.getCustomer().getLastname() == null || dto.getCustomer().getLastname().isEmpty()) {
            throw new ValidationException("لطفا نام خانوادگی خود را وارد کنید");
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new ValidationException("لطفا نام کاربری خود را وارد کنید");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new ValidationException("لطفا رمز عبور خود را وارد کنید");
        }
        if (dto.getCustomer().getPostalCode() == null || dto.getCustomer().getPostalCode().isEmpty()) {
            throw new ValidationException("لطفا کد پستی خود را وارد کنید");
        }
        if (dto.getCustomer().getTel() == null || dto.getCustomer().getTel().isEmpty()) {
            throw new ValidationException("لطفا شماره تماس خود را وارد کنید");
        }
    }

}
