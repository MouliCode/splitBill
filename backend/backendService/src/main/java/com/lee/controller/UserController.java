package com.lee.controller;


import com.lee.dao.User;
import com.lee.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {


    private UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    public User create(@RequestBody User u){
        return service.save(u);
    }

    @GetMapping
    public List<User> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable String id){
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User u){
        return service.updateById(u);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id){
        return service.deleteById(id) ? "deleted": "notDeleted" ;
    }


}
