package com.bicart.controller;

import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.ResponseUserDto;
import com.bicart.dto.UserDto;
import com.bicart.dto.UserRoleDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.UserService;

/**
 * <p>
 * UserController class is a REST controller class that handles all the user related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * <p>
     * Adds a new user to the database.
     * </p>
     *
     * @param user The user details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> addUser(@Validated @RequestBody UserDto user) {
        userService.addUser(user);
        return SuccessResponse.setSuccessResponse("User Registration Successful",
                HttpStatus.CREATED);
    }

    /**
     * <p>
     * Updates the user details.
     * </p>
     *
     * @param user The user details to be updated.
     * @return {@link SuccessResponse} containing user details with {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<SuccessResponse> updateUser(@RequestBody UserDto user) {
        user = userService.updateUser(user);
        return SuccessResponse.setSuccessResponse("User updated successfully",
                HttpStatus.OK, Map.of("user", user));
    }

    /**
     * <p>
     * Gets all the users.
     * </p>
     *
     * @param page page number
     * @param size number of users per page
     * @return {@link SuccessResponse} containing users with {@link HttpStatus} OK
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Set<ResponseUserDto> users = userService.getAllUsers(page, size);
        return SuccessResponse.setSuccessResponse("Users fetched successfully",
                HttpStatus.OK, Map.of("users", users));
    }

    /**
     * <p>
     * Gets a user by its id.
     * </p>
     *
     * @param userId for which user is fetched
     * @return {@link SuccessResponse} containing user with {@link HttpStatus} OK
     */
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse> getUser(@RequestAttribute("id") String userId) {
        UserRoleDto user = userService.getUser(userId);
        return SuccessResponse.setSuccessResponse("User fetched successfully",
                HttpStatus.OK, Map.of("user", user));
    }

    /**
     * <p>
     * Deletes a user.
     * </p>
     *
     * @param userId for which user is updated
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<SuccessResponse> removeUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return SuccessResponse.setSuccessResponse("User deleted successfully",
                HttpStatus.NO_CONTENT);
    }

    /**
     * <p>
     * Authenticates a user and returns the token for the user.
     * </p>
     *
     * @param user The user details to be authenticated.
     * @return {@link SuccessResponse} containing the token with {@link HttpStatus} OK
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@RequestBody UserDto user) {
        String token = userService.authenticateUser(user);
        return SuccessResponse.setSuccessResponse("User authenticated successfully",
                HttpStatus.OK, Map.of("token", token));
    }

    /**
     * <p>
     * Makes a user an admin.
     * </p>
     *
     * @param userDto The user details to be updated.
     * @return {@link SuccessResponse} with {@link HttpStatus} OK
     */
    @PatchMapping("/make-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> makeAdmin(@RequestBody UserDto userDto) {
        userService.makeAdmin(userDto);
        return SuccessResponse.setSuccessResponse("User updated successfully",
                HttpStatus.OK);
    }
}