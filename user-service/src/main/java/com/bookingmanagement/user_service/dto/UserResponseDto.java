package com.bookingmanagement.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserResponseDto {
    private int id;
    private int age;
    private String username;
    private String email;
    private String password;
    private String mobileNo;
}
