package com.bicart.controller;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.ReviewDto;
import com.bicart.dto.UserDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.ReviewService;
import com.bicart.service.UserService;

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
    public ResponseEntity<SuccessResponse> addUser(@Validated @RequestBody UserDto userDto) {
        userService.addUser(userDto);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .message("User added successfully")
                .status("SUCCESS")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Updates the user details.
     * </p>
     * @param userDto The user details to be updated.
     * @return {@link ResponseEntity<UserDto>} user details that were updated with {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<SuccessResponse> updateUser(@RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userDto);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User updated successfully")
                .status("SUCCESS")
                .response(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Set<UserDto> users = userService.getAllUsers(page, size);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Users fetched successfully")
                .status("SUCCESS")
                .response(users)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets a user by its id.
     * </p>
     * @param userId for which user is fetched
     * @return {@link ResponseEntity<UserDto>} user with {@link HttpStatus} OK
     */
    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse> getUserById(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User fetched successfully")
                .status("SUCCESS")
                .response(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<SuccessResponse> getAllReviewsByUserId(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @PathVariable String userId) {
        Set<ReviewDto> reviews = reviewService.getAllReviewsByUserId(userId, page, size);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("Reviews fetched successfully")
                .status("SUCCESS")
                .response(reviews)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes a user.
     * </p>
     * @param userId for which user is updated
     * @return {@link HttpStatus} NO_CONTENT if user is deleted
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<SuccessResponse> removeUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("User deleted successfully")
                .status("SUCCESS")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    /**
     * <p>
     *     Authenticates a user and returns the token for the user.
     * </p>
     * @param userDto The user details to be authenticated.
     * @return {@link ResponseEntity<String>} user details that were authenticated with {@link HttpStatus} OK
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody UserDto userDto) {
        String token = userService.authenticateUser(userDto);
        SuccessResponse response = SuccessResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message("User authenticated successfully")
                .status("SUCCESS")
                .response(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
