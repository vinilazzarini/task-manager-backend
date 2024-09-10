package com.devini.task_management_system.controllers;

import com.devini.task_management_system.domain.User;
import com.devini.task_management_system.dto.LoginRequestDTO;
import com.devini.task_management_system.dto.RegisterRequestDTO;
import com.devini.task_management_system.dto.ResponseDTO;
import com.devini.task_management_system.exceptions.UserAlreadyExistsException;
import com.devini.task_management_system.exceptions.WrongLoginException;
import com.devini.task_management_system.infra.security.TokenService;
import com.devini.task_management_system.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO authData){
        User user = this.repository.findByUsername(authData.login()).orElseThrow(() -> new WrongLoginException("User not found"));
        if(passwordEncoder.matches(authData.password(), user.getPassword())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getUsername(), token));
        }
        throw new WrongLoginException();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO authData){
        Optional<User> user = this.repository.findByUsername(authData.login());
        if (user.isPresent()){
            throw new UserAlreadyExistsException();
        }
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(authData.password()));
        newUser.setUsername(authData.login());
        this.repository.save(newUser);
        String token = this.tokenService.generateToken(newUser);
        return ResponseEntity.ok(new ResponseDTO(newUser.getUsername(), token));
    }
}
