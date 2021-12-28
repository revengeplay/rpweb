package com.example.web.controller;

import com.example.web.Repository.UserRepository;

import com.example.web.model.Board;
import com.example.web.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserApiController {

    @Autowired
    private UserRepository userRepository;


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    List<User> all() {
            return userRepository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newuser(@RequestBody User newuser) {
        return userRepository.save(newuser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceuser(@RequestBody User newuser, @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> {
//                    user.setTitle(newuser.getTitle());
//                    user.setContent(newuser.getContent());
                    user.setBoards(newuser.getBoards());
                    for(Board board : user.getBoards()){
                        board.setUser(user);
                    }


                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newuser.setId(id);
                    return userRepository.save(newuser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteuser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}