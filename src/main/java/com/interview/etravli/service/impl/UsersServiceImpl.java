package com.interview.etravli.service.impl;

import com.interview.etravli.enums.ExceptionMessages;
import com.interview.etravli.exceptions.EntityNotFoundException;
import com.interview.etravli.models.Users;
import com.interview.etravli.repository.UserRepository;
import com.interview.etravli.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    private final UserRepository userRepo;

    @Autowired
    public UsersServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public Users findByUsername(String username) {
        LOGGER.warn("Fetching user by username: {}", username);
        return userRepo.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessages.USER_NOT_FOUND)
        );
    }

}
