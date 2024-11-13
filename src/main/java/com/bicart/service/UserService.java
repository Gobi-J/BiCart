package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bicart.dto.ResponseUserDto;
import com.bicart.dto.UserDto;
import com.bicart.dto.UserRoleDto;
import com.bicart.helper.CustomException;
import com.bicart.helper.UnAuthorizedException;
import com.bicart.mapper.UserMapper;
import com.bicart.model.Role;
import com.bicart.model.User;
import com.bicart.repository.UserRepository;
import com.bicart.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * <p>
     * Saves an user.
     * </p>
     *
     * @param user model.
     */
    public void saveUser(User user) {
        try {
            userRepository.save(user);
            logger.info("User saved successfully with id : {}", user.getId());
        } catch (Exception e) {
            logger.error("Error in saving user with id : {}", user.getId(), e);
            throw new CustomException("Cannot save user. Try Again");
        }
    }

    /**
     * <p>
     * Creates a new User object and saves it in the repository.
     * </p>
     *
     * @param userDTO to create new user.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public void addUser(UserDto userDTO) {
        User user = UserMapper.dtoToModel(userDTO);
        if (userRepository.existsByEmailOrMobileNumber(userDTO.getEmail(), userDTO.getMobileNumber())) {
            logger.error("User with same Email Id or Mobile Number exists");
            throw new DuplicateKeyException("User with same Email or Mobile Number exists");
        }
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAudit(user.getId());
        user.setRole(Set.of(roleService.getRoleModelByName("USER")));
        saveUser(user);
        logger.info("User added successfully with name: {}", user.getName());
    }

    /**
     * <p>
     * Updates the user with new details.
     * </p>
     *
     * @param userDto to update the all the details of the user.
     * @return the updated User object.
     * @throws CustomException when exception is thrown.
     */
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.dtoToModel(userDto);
        saveUser(user);
        logger.info("User updated successfully for ID: {}", userDto.getId());
        return UserMapper.modelToDto(user);
    }

    /**
     * <p>
     * Retrieves and displays all users.
     * </p>
     *
     * @return {@link Set <UserDto>} all the Users.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<ResponseUserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> userPage = userRepository.findAllByIsDeletedFalse(pageable);
        logger.info("Displayed user details for page : {}", page);
        return userPage.stream()
                .map(UserMapper::modelToResponseUserDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves and displays the details of an user.
     * </p>
     *
     * @param id the ID of the user whose details are to be viewed
     * @return the User object.
     * @throws NoSuchElementException when occurred.
     */
    public UserRoleDto getUser(String id) throws NoSuchElementException, CustomException {
        User user = userRepository.findByIdAndIsDeletedFalse(id);
        if (user == null) {
            logger.warn("User not found for the given id: {}", id);
            throw new NoSuchElementException("User not found for the given id: " + id);
        }
        logger.info("Retrieved user details for ID: {}", id);
        return UserMapper.modelToUserRoleDto(user);
    }

    /**
     * <p>
     * Retrieves an user model.
     * </p>
     *
     * @param id of a user.
     * @return the User model
     */
    protected User getUserModelById(String id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id);
        if (user == null) {
            logger.error("User not found for the given id: {}", id);
            throw new NoSuchElementException("User not found for the given id: " + id);
        }
        logger.info("Retrieved user for the given id: {}", id);
        return user;
    }

    /**
     * <p>
     * Deletes an user based on the provided ID.
     * </p>
     *
     * @param id of the user for deleting.
     * @throws NoSuchElementException and CustomException.
     */
    public void deleteUser(String id) throws NoSuchElementException, CustomException {
        User user = getUserModelById(id);
        Set<Role> roles = user.getRole();
        if (roles != null && !roles.isEmpty()) {
            user.setRole(null);
        }
        user.setIsDeleted(true);
        saveUser(user);
        logger.info("User removed successfully with ID: {}", id);
    }

    /**
     * <p>
     * Login an user based on the provided Username and password. Generates new token for a user.
     * </p>
     *
     * @param userDTO object.
     * @return {@link String} created token.
     * @throws UnAuthorizedException when user is not authorized.
     */
    public String authenticateUser(UserDto userDTO) {
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDTO.getEmail(), userDTO.getPassword()
                            )
                    );
            return JwtUtil.generateToken(userDTO.getEmail());
        } catch (BadCredentialsException e) {
            logger.error("Error in user login with id: {} ", userDTO.getId(), e);
            throw new UnAuthorizedException("Invalid Username or Password");
        }
    }

    public void makeAdmin(UserDto userDto) {
        User user = getUserModelById(userDto.getId());
        try {
            Set<Role> roles = user.getRole();
            roles.add(roleService.getRoleModelByName("ADMIN"));
            user.setRole(roles);
        } catch (Exception e) {
            logger.error("Error in setting role for user", e);
            throw new CustomException("Cannot apply role to an user");
        }
        saveUser(user);
        logger.info("User updated successfully for ID: {}", userDto.getId());
    }
}

