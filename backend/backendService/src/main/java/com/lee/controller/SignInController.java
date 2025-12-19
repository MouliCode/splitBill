package com.lee.controller;

import com.lee.dao.SignInCredentials;
import com.lee.service.SignInService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/signin")
public class SignInController {

    private SignInService service;

    public SignInController(SignInService service ){
        this.service = service;
    }

    @PostMapping
    public SignInCredentials create(@RequestBody SignInCredentials cred){
       return service.save(cred);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.deleteById(id);
    }
}
