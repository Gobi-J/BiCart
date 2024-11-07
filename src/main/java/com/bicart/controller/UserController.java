package com.bicart.controller;

import com.bicart.dto.ReviewDto;
import com.bicart.dto.UserDto;
import com.bicart.service.ReviewService;
import com.bicart.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 *     UserController class is a REST controller class that handles all the user related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    /**
     * <p>
     *     Adds a new user to the database.
     * </p>
     * @param userDto The user details to be added.
     * @return {@link ResponseEntity<UserDto>} user details that were added with {@link HttpStatus} CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Updates the user details.
     * </p>
     * @param userDto The user details to be updated.
     * @return {@link ResponseEntity<UserDto>} user details that were updated with {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets all the users.
     * </p>
     * @param page page number
     * @param size number of users per page
     * @return {@link ResponseEntity<Set<UserDto>} users with {@link HttpStatus} OK
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<UserDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(userService.getAllUsers(page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets a user by its id.
     * </p>
     * @param userId for which user is fetched
     * @return {@link ResponseEntity<UserDto>} user with {@link HttpStatus} OK
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets all the reviews by a user.
     * </p>
     * @param userId for which reviews are fetched
     * @param page page number
     * @param size number of reviews per page
     * @return {@link ResponseEntity<Set<ReviewDto>} reviews with {@link HttpStatus} OK
     */
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<Set<ReviewDto>> getAllReviewsByUserId(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @PathVariable String userId) {
        return new ResponseEntity<>(reviewService.getAllReviewsByUserId(userId, page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes a user.
     * </p>
     * @param userId for which user is updated
     * @return {@link HttpStatus} NO_CONTENT if user is deleted
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * <p>
     *     Authenticates a user and returns the token for the user.
     * </p>
     * @param userDto The user details to be authenticated.
     * @return {@link ResponseEntity<String>} user details that were authenticated with {@link HttpStatus} OK
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.authenticateUser(userDto), HttpStatus.OK);
    }
}
