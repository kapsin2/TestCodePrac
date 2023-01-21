package com.example.dto;

public class AuthenticatedUserDto {

    private final String userRoleEnum;
    private final String username;

    public AuthenticatedUserDto(String userRoleEnum, String username) {
        this.userRoleEnum = userRoleEnum;
        this.username = username;
    }
}
