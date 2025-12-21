package com.lee.controller;

import com.lee.dao.User;
import com.lee.service.SignUpService;
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
    public User create (@RequestBody User user){
        return service.save(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id){
       return service.findById(id);
    }

    @PatchMapping
    public User updateUser(@RequestBody User user){
        return service.updateById(user);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable String id){
        return service.deleteById(id);
    }
}
