package com.springboot.blog.service;

import com.springboot.blog.payload.JwtAuthResponse;
import com.springboot.blog.payload.LoginD;
import com.springboot.blog.payload.SignUpDto;

public interface AuthService {
    public JwtAuthResponse login(LoginD loginDto);
    public String signIn( SignUpDto signUpDto);
}
