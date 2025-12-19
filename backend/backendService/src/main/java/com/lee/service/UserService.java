package com.lee.service;


import com.lee.dao.User;
import com.lee.dao.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo){
        this.repo = repo;
    }

    @Transactional
    public User save(User u){
        return repo.save(u);
    }

    public List<User> getAll(){
        return repo.findAll();
    }

    public Optional<User> findById(String phoneNo){
        return repo.findById(Integer.valueOf(phoneNo));
    }

    @Transactional
    public boolean deleteById(String phoneNo){
        if(!repo.existsById(Integer.valueOf(phoneNo))) return false;
        repo.deleteById(Integer.valueOf(phoneNo));
        return true;
    }

    @Transactional
    public User updateById(User user){
        User existing = repo.findById(Integer.valueOf(user.getPhoneNo())).orElse(null);

        if(existing == null){
            return null;
        }
        existing.setName(user.getName());
        existing.setBalance(user.getBalance());
        existing.setToGet(user.getToGet());
        existing.setToGive(user.getToGive());

        return existing;
    }
}
