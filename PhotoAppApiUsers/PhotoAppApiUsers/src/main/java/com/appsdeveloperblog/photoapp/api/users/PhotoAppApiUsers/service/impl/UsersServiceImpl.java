package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.service.impl;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.DTO.UserDTO;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.entity.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.repository.UsersRepository;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import jakarta.ws.rs.NotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity user = mapper.map(userDto, UserEntity.class);

        usersRepository.save(user);
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity user = usersRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account dont exist in system"));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(user, UserDTO.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account dont exist in system"));

        return new User(
                user.getEmail(),
                user.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}
