package com.example.eventbot.services;

import com.example.eventbot.model.User;
import com.example.eventbot.model.dto.UserDto;
import com.example.eventbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void create(UserDto dto) {
        User user = new User();
        userRepository.save(user);
    }

    public void delete()
}
