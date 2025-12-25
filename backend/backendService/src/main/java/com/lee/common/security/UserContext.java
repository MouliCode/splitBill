package com.lee.common.security;

import java.util.UUID;

public class UserContext {

    private static final ThreadLocal<UUID> CURRENT_USER = new ThreadLocal<>();

    public static void setUserId(UUID userId){
        CURRENT_USER.set(userId);
    }

    public static UUID getUserId(){
        return CURRENT_USER.get();
    }

    public static void clear(){
        CURRENT_USER.remove();
    }
}
