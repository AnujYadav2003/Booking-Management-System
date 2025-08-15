package com.bookingmanagement.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserRequestDto {

    private int age;
    private String username;
    private String email;
    private String password;
    private String mobileNo;
}
