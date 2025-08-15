package com.bookingmanagement.user_service.controller;

import com.bookingmanagement.user_service.dto.UserRequestDto;
import com.bookingmanagement.user_service.dto.UserResponseDto;
import com.bookingmanagement.user_service.model.UserModel;
import com.bookingmanagement.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService)
    {
        this.userService=userService;
    }


    @PostMapping()
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRequestDto userRequestDto)
    {
        UserResponseDto user= userService.registerUser(userRequestDto);
        return ResponseEntity.ok(user);
    }



    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable int id)
    {
        UserResponseDto user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id,@RequestBody UserRequestDto userRequestDto)
    {
        UserResponseDto user=userService.updateUser(id,userRequestDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id)
    {
        String mess=userService.deleteUser(id);
        return ResponseEntity.ok(mess);
    }


    //add only login/logout endpoints---
}
