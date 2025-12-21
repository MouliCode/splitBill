package com.lee.controller;

import com.lee.dao.User;
import com.lee.response.SignInResponse;
import com.lee.service.SignInService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/signin")
public class SignInController {

    private SignInService service;

    public SignInController(SignInService service ){
        this.service = service;
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id){
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.deleteById(id);
    }
}
