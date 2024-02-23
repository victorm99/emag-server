package com.emag.service;

import com.emag.controller.UserController;
import com.emag.exception.BadRequestException;
import com.emag.exception.NotFoundException;
import com.emag.exception.UnauthorizedException;
import com.emag.model.dto.AddressDTO;
import com.emag.model.dto.register.RegisterRequestUserDTO;
import com.emag.model.dto.register.RegisterResponseUserDTO;
import com.emag.model.dto.user.*;
import com.emag.model.pojo.Address;
import com.emag.model.pojo.User;
import com.emag.util.ImageUtil;
import com.emag.util.UserUtility;
import lombok.SneakyThrows;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class UserService extends AbstractService {

    public RegisterResponseUserDTO register(RegisterRequestUserDTO u) {
        String email = u.getEmail().trim();
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("Email already exists!");
        }
        if (!UserUtility.isValidEmail(email)) {
            throw new BadRequestException("Invalid email!");
        }
        String password = u.getPassword();
        if (!UserUtility.isValidPass(password)) {
            throw new BadRequestException("Invalid password!");
        }
        if (!UserUtility.passwordsMatch(u.getPassword() , u.getConfirmPassword())) {
            throw new BadRequestException("Passwords mismatch!");
        }
        if (!UserUtility.isValidName(u.getFullName())) {
            throw new BadRequestException("Invalid name format!");
        }
        String encodedPass = passwordEncoder.encode(password);
        User user = modelMapper.map(u , User.class);
        user.setPassword(encodedPass);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user = userRepository.save(user);
        return modelMapper.map(user, RegisterResponseUserDTO.class);
    }

    public UserWithoutPasswordDTO login(LoginRequestUserDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        User userFromDb = userRepository.findByEmail(email);
        if (userFromDb == null) {
            throw new NotFoundException("Wrong email!");
        }
        String passwordFromDb = userFromDb.getPassword();
        if (!passwordEncoder.matches(password, passwordFromDb)) {
            throw new NotFoundException("Wrong password!");
        }
        return modelMapper.map(userFromDb, UserWithoutPasswordDTO.class);
    }


    public UserWithoutPasswordDTO findById(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new BadRequestException("User does not exist!");
        }
        return modelMapper.map(user, UserWithoutPasswordDTO.class);
    }


    public UserWithoutPasswordDTO editUserData(long id, EditUserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        String mobilePhone = dto.getMobilePhone();
        if (mobilePhone != null) {
            int l = mobilePhone.length();
            if (userRepository.findByMobilePhoneContaining(mobilePhone.substring(l - 9, l - 1)).isPresent()){
                throw new BadRequestException("Duplicated mobile phone!");
            }
            if (!UserUtility.isValidMobilePhone(mobilePhone)) {
                throw new BadRequestException("Invalid mobile phone!");
            }
            user.setMobilePhone(mobilePhone);
        }
        LocalDate birthDate = dto.getBirthDate();
        if (birthDate != null) {
            if (!UserUtility.isValidBirthDate(birthDate)) {
                throw new BadRequestException("Invalid birth date!");
            }
            user.setBirthDate(birthDate);
        }
        AddressDTO addressDTO = dto.getAddress();
        if (addressDTO != null) {
            Address address = UserUtility.validateAddress(addressDTO);
            boolean addressExists = UserUtility.adressExists(address, user);
            if (addressExists) {
                throw new BadRequestException("Address already added!");
            }
            address = addressRepository.save(address);
            user.getAddresses().add(address);
        }
        String nickname = dto.getNickname();
        if (nickname != null ) {
            if (nickname.trim().length()<=1 || nickname.trim().length()>45){
                throw new BadRequestException("Nickname must be between 2 and 45 symbols");
            }
            user.setNickname(nickname);
        }
        String gender = dto.getGender();
        if (gender != null) {
            if (!Arrays.asList(GENDER).contains(gender.toLowerCase().trim())){
                throw new BadRequestException("Unknown gender");
            }
            user.setGender(gender);
        }
        String email = dto.getEmail();
        if (email != null){
            if (userRepository.findByEmail(email) != null){
                throw new BadRequestException("Email already exists!");
            }
            if (!UserUtility.isValidEmail(email)){
                throw new BadRequestException("Invalid email!");
            }
            user.setEmail(email);
        }
        user =userRepository.save(user);
        return modelMapper.map(user,UserWithoutPasswordDTO .class);
    }

    public UserWithoutPasswordDTO editUserPassword(long id, EditPasswordRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        String oldPass = dto.getOldPassword();
        String newPass = dto.getNewPassword();
        String newConfirmPass = dto.getConfirmNewPassword();
        boolean oldPassMatches = passwordEncoder.matches(oldPass , user.getPassword());
        boolean newPassConfirmed = UserUtility.passwordsMatch(newPass, newConfirmPass);
        boolean newPassIsValid = UserUtility.isValidPass(newPass);
        if (!oldPassMatches) {
            throw new UnauthorizedException("Wrong password!");
        }
        if (!newPassIsValid) {
            throw new BadRequestException("New password is invalid !");
        }
        if (!newPassConfirmed) {
            throw new BadRequestException("Passwords mismatch !");
        }
        user.setPassword(passwordEncoder.encode(newPass));
        user =userRepository.save(user);
        return modelMapper.map(user,UserWithoutPasswordDTO .class);
    }


    @SneakyThrows
    public String uploadImage(MultipartFile file, long id) {
        String name = ImageUtil.validateImageAndReturnName(file);
        File f = new File("user" + File.separator + "uploads" + File.separator + name);
        Files.copy(file.getInputStream() ,
                f.toPath() , StandardCopyOption.REPLACE_EXISTING);
        User u = userRepository.getById(id);
        u.setImageUrl(f.getPath());
        userRepository.save(u);
        return f.toPath().toString();
    }

    public UserWithoutPasswordDTO subscribe(User user) {
        if (user.isSubscribed()){
            throw new BadRequestException("Already subscribed!");
        }
        user.setSubscribed(true);
        return modelMapper.map(userRepository.save(user) , UserWithoutPasswordDTO.class);
    }

    public UserWithoutPasswordDTO unsubscribe(User user) {
        if (!user.isSubscribed()){
            throw new BadRequestException("Not subscribed!");
        }
        user.setSubscribed(false);
        return modelMapper.map(userRepository.save(user) , UserWithoutPasswordDTO.class);
    }


    public UserWithoutPasswordDTO forgottenPassword(ForgottenPassDTO dto) {
        String email = dto.getEmail();
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new NotFoundException("Email not found!");
        }
        String password = UserUtility.generateRandomPassword();
        if (!UserUtility.isValidPass(password)){
            throw new BadRequestException("Bad password generated!");
        }
        String msg = "Your new autogenerated password is: \n " +
                password + " \n" +
                "You can change it anytime in account settings.";
        new Thread(() -> emailService.sendSimpleMessage(email , "Password recovery" , msg)).start();
        user.setPassword(passwordEncoder.encode(password));
        return modelMapper.map(userRepository.save(user) , UserWithoutPasswordDTO.class);
    }
}
