package com.lee.dto.response.groupresponsedtos;

import java.util.UUID;

public record GroupMemberResponse(
        UUID id,
        String name
) {
}
