package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.service;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDTO createUser(UserDTO userDto);

    UserDTO getUserDetailsByEmail(String email);
}
