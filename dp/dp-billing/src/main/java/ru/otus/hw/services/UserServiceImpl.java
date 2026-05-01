package ru.otus.hw.services;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.repositories.RoleRepository;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public void create(String userName) {
        Set<Role> role = new HashSet<>();
        role.add(roleRepository.findById(2L).get());
        userRepository.save(new User(0l, userName, userName, role));
        log.info("Added User: {}", userName);
    }
}
