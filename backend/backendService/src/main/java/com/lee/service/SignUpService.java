package com.lee.service;

import com.lee.dao.SignUpUser;
import com.lee.dao.repo.SignUpUserRepository;
import com.lee.response.signUpResponse.SignUpResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SignUpService extends SignUpUser {

    private final SignUpUserRepository repo;
    private final SignUpResponse response;

    public SignUpService(SignUpUserRepository repo, SignUpResponse response){
        this.repo = repo;
        this.response = response;
    }

    @Transactional
    public SignUpUser save(SignUpUser user){
        return response.createResponse(repo.save(user));
    }

    public Optional<SignUpUser> findById(String userID){
        return Optional.ofNullable(response.createResponse(repo.findById(userID).get()));
    }

    public List<SignUpUser> findAll(){
        return Collections.singletonList(response.createResponse((SignUpUser) repo.findAll()));
    }

    public boolean deleteById(String userID){
        var existingUser = repo.findById(userID);

        if(existingUser != null){
            repo.deleteById(userID);
            return true;
        }

        return false;
    }

    @Transactional
    public SignUpUser updateById(SignUpUser user){
        Optional<SignUpUser> existing = repo.findById(user.getUserId());

        if(existing == null){
            return null;
        }
        var existingUser = existing.get();

        existingUser.setFirstName(user.getFirstName());
        existingUser.setEmailId(user.getEmailId());
        existingUser.setLastName(user.getLastName());
        existingUser.setPassword(user.getPassword());
        existingUser.setPhoneNo(user.getPhoneNo());

        return existingUser;
    }
}
