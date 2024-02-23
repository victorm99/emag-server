package com.emag.controller;

import com.emag.exception.BadRequestException;
import com.emag.exception.UnauthorizedException;
import com.emag.model.dto.register.RegisterRequestUserDTO;
import com.emag.model.dto.register.RegisterResponseUserDTO;
import com.emag.model.dto.user.*;
import com.emag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/users")
    public ResponseEntity<RegisterResponseUserDTO> register(@RequestBody @Valid RegisterRequestUserDTO u , HttpSession session){
        sessionManager.isLoggedVerification(session);
        return ResponseEntity.ok(userService.register(u));
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserWithoutPasswordDTO> login(@RequestBody @Valid LoginRequestUserDTO dto , HttpServletRequest request){
        UserWithoutPasswordDTO user = userService.login(dto);
        sessionManager.loginUser(request , user.getId());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<LogoutDTO> logout(HttpSession session){
        sessionManager.logoutUser(session);
        return ResponseEntity.ok(new LogoutDTO("Logout successful!"));
    }

    @GetMapping("/users/{id}")
    public UserWithoutPasswordDTO getUserById(@PathVariable long id, HttpServletRequest request) {
        if (!sessionManager.userHasPrivileges(request, id)) {
            throw new UnauthorizedException("No privileges!");
        }
        return userService.findById(id);
    }

    @Transactional
    @PutMapping("/users/{id}")
    public UserWithoutPasswordDTO editUserData(@PathVariable long id, @RequestBody @Valid EditUserRequestDTO dto, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request , id)){
            throw new UnauthorizedException("No privileges!");
        }
        return userService.editUserData(id,dto);
    }

    @Transactional
    @PutMapping("/users/{id}/pass")
    public UserWithoutPasswordDTO editUserPassword(@PathVariable long id, @RequestBody @Valid EditPasswordRequestDTO dto, HttpServletRequest request){
        if (!sessionManager.userHasPrivileges(request , id)){
            throw new UnauthorizedException("No privileges!");
        }
        return userService.editUserPassword(id,dto);
    }

    @Transactional
    @PutMapping("/users/forgotten-pass")
    public ResponseEntity<UserWithoutPasswordDTO> forgottenPassword (@RequestBody @Valid ForgottenPassDTO dto, HttpServletRequest request){
        sessionManager.isLoggedVerification(request.getSession());
        return ResponseEntity.ok(userService.forgottenPassword (dto));
    }

    @PostMapping("/users/{id}/image")
    public String uploadImage( @RequestPart MultipartFile file, @PathVariable long id, HttpServletRequest request ) {
        if(!sessionManager.userHasPrivileges(request, id)){
            throw new BadRequestException("No privileges!");
        }
        return userService.uploadImage(file,id);
    }

    @PostMapping("/users/subscribe")
    public UserWithoutPasswordDTO subscribe(HttpServletRequest request){
        return userService.subscribe(sessionManager.getLoggedUser(request));
    }

    @PostMapping("/users/unsubscribe")
    public UserWithoutPasswordDTO unsubscribe(HttpServletRequest request){
        return userService.unsubscribe(sessionManager.getLoggedUser(request));
    }
}
