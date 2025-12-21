package com.lee.service;

import com.lee.dao.User;
import com.lee.dao.repo.UserRepository;
import com.lee.response.SignUpResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SignUpService extends User {

    private final UserRepository repo;
    private final SignUpResponse response;

    public SignUpService(UserRepository repo, SignUpResponse response){
        this.repo = repo;
        this.response = response;
    }

    @Transactional
    public User save(User user){
        return response.createResponse(repo.save(user));
    }

    public Optional<User> findById(String userID){
        return Optional.ofNullable(response.createResponse(repo.findById(userID).get()));
    }

    public List<User> findAll(){
        return Collections.singletonList(response.createResponse((User) repo.findAll()));
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
    public User updateById(User user){
        Optional<User> existing = repo.findById(user.getUserId());

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
