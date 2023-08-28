package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.repository;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {



}
