package com.springboot.blog.controller;

import com.springboot.blog.payload.JwtAuthResponse;
import com.springboot.blog.payload.LoginD;
import com.springboot.blog.payload.SignUpDto;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping(value={"/login","signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginD loginD){
        JwtAuthResponse response = authService.login(loginD);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"register","signup"})
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        String resp = authService.signIn(signUpDto);
        return  new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

}
