package com.example.yousignproto.model;

public record AddFieldDto(
        String type,
        String signer_id,
        int page,
        int x,
        int y
) {
}
