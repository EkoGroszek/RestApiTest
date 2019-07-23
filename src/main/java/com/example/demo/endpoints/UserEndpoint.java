package com.example.demo.endpoints;

import com.example.demo.entities.User;
import com.example.demo.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserEndpoint {

    private final UserServiceImpl userService;

    @Autowired
    public UserEndpoint(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<Iterable<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

}
