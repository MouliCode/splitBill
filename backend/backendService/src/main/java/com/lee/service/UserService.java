package com.lee.service;


import com.lee.dao.User;
import com.lee.dao.repo.UserRepository;
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

    public Optional<User> findById(String id){
        return repo.findById(id);
    }

    @Transactional
    public boolean deleteById(String id){
        if(!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

}
