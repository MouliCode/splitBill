package com.lee.dto.request.groupdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateGroupRequest(
        @NotBlank String name,
        @NotEmpty List<UUID> memberIds) {}
