package com.lee.response.signUpResponse;


import com.lee.dao.SignUpUser;
import org.springframework.stereotype.Component;

@Component
public class SignUpResponse {



    public SignUpUser createResponse(SignUpUser user){

        var response = new SignUpUser();
        response.setUserId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmailId(user.getEmailId() );

        return response;
    }
}
