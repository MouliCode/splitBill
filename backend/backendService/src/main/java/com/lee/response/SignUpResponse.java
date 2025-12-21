package com.lee.response;


import com.lee.dao.User;
import org.springframework.stereotype.Component;

@Component
public class SignUpResponse {



    public User createResponse(User user){

        var response = new User();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmailId(user.getEmailId() );

        return response;
    }
}
