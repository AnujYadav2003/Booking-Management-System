package com.bookingmanagement.user_service.service;

import com.bookingmanagement.user_service.Mapper.UserMapper;
import com.bookingmanagement.user_service.dto.UserRequestDto;
import com.bookingmanagement.user_service.dto.UserResponseDto;
import com.bookingmanagement.user_service.exceptions.UserNotFoundException;
import com.bookingmanagement.user_service.model.UserModel;
import com.bookingmanagement.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }


    public UserResponseDto registerUser(UserRequestDto userRequestDto) {

        String email=userRequestDto.getEmail();
        boolean checkEmail = userRepository.findByEmail(email).isPresent();
        if(checkEmail==true){
            throw new RuntimeException("User already exists");
        }
        UserModel user=new UserModel();
        user.setAge(userRequestDto.getAge());
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setMobileNo(userRequestDto.getMobileNo());
        userRepository.save(user);
//        return user;
        UserResponseDto newUser=UserMapper.toDto(user);
        return newUser;
    }



    public UserResponseDto getUserById(int id) {
        UserModel isExists=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with UserID "+id));
//       return isExists;
        UserResponseDto user=UserMapper.toDto(isExists);
        return user;

    }

    public UserResponseDto updateUser(int id,UserRequestDto userRequestDto){

        UserModel isExists=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with UserID"+id));
         UserModel user=isExists;
         user.setAge(userRequestDto.getAge());
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setMobileNo(userRequestDto.getMobileNo());
        userRepository.save(user);
        UserResponseDto updateUser=UserMapper.toDto(user);
        return updateUser;
    }

    public String deleteUser(int id)
    {
        UserModel isExists=userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found with USerID"+id));

         userRepository.deleteById(id);
        return "User Deleted successfully";
    }
}

//user
//flight
//booking
//
