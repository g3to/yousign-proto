package com.example.yousignproto.model;

public record AddSignerDto(
        SignerInfoDto info,
        String signature_level,
        String signature_authentication_mode
) {
}
