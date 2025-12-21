package com.lee.service;

import com.lee.dao.User;
import com.lee.dao.repo.UserRepository;
import com.lee.response.SignInResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignInService {


    private  final UserRepository repo;
    private final SignInResponse response;

    public SignInService(UserRepository repo, SignInResponse response) {
        this.repo = repo;
        this.response = response;
    }

    public Optional<User> findById(String id){
        return Optional.ofNullable(response.SignInResponse(repo.findById(id).get()));
    }

    @Transactional
    public boolean deleteById(String id){

        var existingUser = findById(id);

        if(existingUser == null) return false;
        return deleteById(id);

    }



}
