package com.lee.controller;

import com.lee.dao.SignUpUser;
import com.lee.response.signUpResponse.SignUpResponse;
import com.lee.service.SignUpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    public SignUpService service;

    public SignUpController(SignUpService service){
        this.service = service;
    }

    @PostMapping
    public SignUpUser create (@RequestBody SignUpUser user){
        return service.save(user);
    }

    @GetMapping("/{id}")
    public Optional<SignUpUser> getUser(@PathVariable String id){
       return service.findById(id);
    }

    @PatchMapping
    public SignUpUser updateUser(@RequestBody SignUpUser user){
        return service.updateById(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable String id){
        return service.deleteById(id);
    }
}
