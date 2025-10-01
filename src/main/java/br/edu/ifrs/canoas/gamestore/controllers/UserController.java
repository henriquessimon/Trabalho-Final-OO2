package br.edu.ifrs.canoas.gamestore.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.canoas.gamestore.model.domain.User;
import br.edu.ifrs.canoas.gamestore.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam String email) {
        User user = userService.getUser(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(user);
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
}
