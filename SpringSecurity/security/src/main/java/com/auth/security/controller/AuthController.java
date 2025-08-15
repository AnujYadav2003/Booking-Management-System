package com.auth.security.controller;

import com.auth.security.dto.AuthRequest;
import com.auth.security.model.User;
import com.auth.security.repository.UserRepository;
import com.auth.security.service.MyUserDetailsService;
import com.auth.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
    public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private MyUserDetailsService userDetailsService;
        //        private com.auth.security.service.MyUserDetailsService userDetailsService;
//        private MyUserDetailsService myUserDetailsService;
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private PasswordEncoder passwordEncoder;


//        @GetMapping("/getByEmail/{email}")
//        public boolean checkUserByEmail(@PathVariable String email){
//            return userRepository.findByEmailForBooking(email);
//        }

        @PostMapping("/register")
        public String registerUser(@RequestBody User user) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return "User already exists with this email.";
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "User registered successfully!";
        }


        @PostMapping("/login")
        public String createAuthenticationToken(@RequestBody AuthRequest authRequest) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

//        final UserDetails userDetails = MyUserDetailsService.loadUserByUsername(authRequest.getEmail());
            return jwtUtil.generateToken(userDetails.getUsername());
        }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(token); // this will also throw if invalid or expired
            return ResponseEntity.ok("Token is valid for user: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

        @GetMapping("/hello")
        public String hello() {
            return "Hello, Authenticated User!";
        }
    }
