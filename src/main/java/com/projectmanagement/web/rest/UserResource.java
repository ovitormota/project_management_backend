package com.projectmanagement.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectmanagement.domain.User;
import com.projectmanagement.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     *
     * @return a list of users.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Request to get all Users");
        List<User> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Get a user by id.
     *
     * @param id the id of the user.
     * @return the ResponseEntity with status 200 (OK) and with body the user,
     *         or with status 404 (Not Found) if the user is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.debug("Request to get User : {}", id);
        Optional<User> user = userService.findOne(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete the user by id.
     *
     * @param id the id of the user to delete.
     * @return the ResponseEntity with status 204 (NO_CONTENT).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("Request to delete User : {}", id);
        if (!userService.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
