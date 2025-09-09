package com.example.yousignproto.model;

public record SignerInfoDto(
        String first_name,
        String last_name,
        String email,
        String locale
) {
}
