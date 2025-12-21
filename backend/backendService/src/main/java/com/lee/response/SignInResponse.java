package com.lee.response;

import com.lee.dao.User;
import org.springframework.stereotype.Component;


@Component
public class SignInResponse {

    public SignInResponse(User user) {}

    public User SignInResponse(User user){
        
        var response = new User();
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        
        return response;
        
    }
}
