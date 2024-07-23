package com.rayan.planner.activity.dto;

import com.rayan.planner.activity.Activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponsePayload(

    UUID activity_id,
    String title,
    LocalDateTime occurs_at
) {
    public static ActivityResponsePayload toActivityResponse(Activity activity) {
        return new ActivityResponsePayload(activity.getId(), activity.getTitle(), activity.getOccursAt());
    }
}
