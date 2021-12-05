package com.cdp.tdp.controller;

import com.cdp.tdp.domain.User;
import com.cdp.tdp.dto.*;
import com.cdp.tdp.security.UserDetailsImpl;
import com.cdp.tdp.security.UserDetailsServiceImpl;
import com.cdp.tdp.service.UserService;
import com.cdp.tdp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody UserDto userDto) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인 실패");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userDto.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity createUser(@RequestBody SignupRequestDto userDto) throws Exception {
        userService.registerUser(userDto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping(value = "/user")
    public User readUser(@AuthenticationPrincipal UserDetailsImpl userDetails) throws SQLException {
        User user = (User) userDetails.getUser();
        return user;
    }

    @GetMapping("/til/ranker")
    public List<UserTilCountDto> getAllUser(){
        return userService.getAllUser();
    }

    @PutMapping("/user/profile")
    public void updateUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestParam("nickname") String nickname,
                           @RequestParam("github_id") String githubId,
                           @RequestParam(value = "file", required = false) MultipartFile imageFile,
                           @RequestParam("about") String about){
        userService.updateUser(userDetails.getUser(), nickname, githubId, imageFile, about);
    }
}
