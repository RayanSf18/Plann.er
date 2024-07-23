package com.rayan.planner.link.dto;

import com.rayan.planner.link.Link;

import java.util.UUID;

public record LinkResponsePayload(
    UUID link_id,
    String title,
    String url
) {
    public static LinkResponsePayload toLinkResponse(Link link) {
        return new LinkResponsePayload(link.getId(), link.getTitle(), link.getUrl());
    }
}
