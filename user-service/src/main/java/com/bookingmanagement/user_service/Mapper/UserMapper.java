package com.bookingmanagement.user_service.Mapper;

import com.bookingmanagement.user_service.dto.UserResponseDto;
import com.bookingmanagement.user_service.model.UserModel;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponseDto toDto(UserModel model) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setAge(model.getAge());
        dto.setPassword(model.getPassword());
        dto.setMobileNo(model.getMobileNo());
        return dto;
    }


    // Converts a list of FlightModel to a list of FlightResponseDto
    public static List<UserResponseDto> toDtoList(List<UserModel> models) {
        return models.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    private UserMapper() {
    }
}

//Exception
//rest templete
//api gateway+api modification
//authenication (sat,sun)
// kafka (notification service)
//
