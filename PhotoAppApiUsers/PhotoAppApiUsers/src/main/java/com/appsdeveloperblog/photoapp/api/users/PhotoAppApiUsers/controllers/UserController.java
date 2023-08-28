package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.DTO.UserDTO;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.request.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.response.CreateUserResponse;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    private UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + env.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequestModel request) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.map(
                        usersService.createUser(mapper.map(request, UserDTO.class)),
                        CreateUserResponse.class
                        ));
    }
}
