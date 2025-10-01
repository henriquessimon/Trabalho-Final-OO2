package br.edu.ifrs.canoas.gamestore.service;
import br.edu.ifrs.canoas.gamestore.model.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifrs.canoas.gamestore.model.domain.User;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getById(Long userId) {
        return userRepository.findById(userId);
    }


    public User registerUser(User user) {
        return userRepository.save(user);
    }
}