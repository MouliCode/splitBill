package com.lee.dto.response.userresponsedtos;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email, String phoneNo) {
}
