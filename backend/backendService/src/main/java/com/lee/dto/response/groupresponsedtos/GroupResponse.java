package com.lee.dto.response.groupresponsedtos;

import java.util.List;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String name,
        List<GroupMemberResponse> members

) {
}
