package com.lee.dto.response.apiResponsedtos;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error){
    public static <T> ApiResponse success(T data){
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse error(String code, String message){
        return new ApiResponse<>(false, null, new ApiError(code, message));
    }

}
