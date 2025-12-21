package com.lee.service;

import com.lee.dao.SignInCredentials;
import com.lee.dao.repo.SingInCreadentialsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SignInService {


    private  final SingInCreadentialsRepository repo;

    public SignInService(SingInCreadentialsRepository repo) {this.repo = repo;}

    public Optional<SignInCredentials> findById(String id){
        return repo.findById(id);
    }

    @Transactional
    public SignInCredentials save(SignInCredentials cred){
        return repo.save(cred);
    }

    @Transactional
    public boolean deleteById(String id){

        var existingUser = findById(id);

        if(existingUser == null) return false;
        return deleteById(id);

    }



}
