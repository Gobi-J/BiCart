package com.bicart.service;

import com.bicart.dto.ReviewDto;
import com.bicart.dto.UserDto;
import com.bicart.helper.CustomException;
import com.bicart.helper.UnAuthorizedException;
import com.bicart.mapper.UserMapper;
import com.bicart.model.Address;
import com.bicart.model.Role;
import com.bicart.model.User;
import com.bicart.repository.UserRepository;
import com.bicart.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final ProductService productService;
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
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error in saving user");
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Creates a new User object and saves it in the repository.
     * </p>
     *
     * @param userDTO to create new user.
     * @return the created userDto object.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public UserDto addUser(UserDto userDTO) throws DuplicateKeyException, CustomException {
        try {
            User user = UserMapper.dtoToModel((userDTO));
            if (userRepository.existsByEmailAndIsDeletedFalse(userDTO.getEmail())) {
                throw new DuplicateKeyException("User exists with same Email Id");
            }
            user.setId(UUID.randomUUID().toString());
            user.setPassword(encoder.encode(userDTO.getPassword()));
            user.setCreatedAt(new Date());
            user.setCreatedBy(user.getId());
            user.setIsDeleted(false);
            if (userDTO.getRole() != null) {
                System.out.println(userDTO.getRole());
                Set<Role> roles = new HashSet<>();
                userDTO.getRole().forEach(roleDto -> {
                    Role role = roleService.getRoleByName(roleDto.getRoleName());
                    roles.add(role);
                });
                user.setRole(roles);
            }
            userRepository.save(user);
            UserDto userDto = UserMapper.modelToDto((user));
            logger.info("User added successfully with ID: {}", userDto.getId());
            return userDto;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                logger.error("User already exists with same Email Id");
                throw e;
            }
            logger.error("Error adding an user", e);
            throw new CustomException("Server Error!!!!", e);
        }
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
    public UserDto updateUser(UserDto userDto) throws CustomException {
        try {
            User user = UserMapper.dtoToModel((userDto));
            userRepository.save(user);
            logger.info("User updated successfully for ID: {}", userDto.getId());
            return UserMapper.modelToDto((user));
        } catch (Exception e) {
            logger.error("Error updating user", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all users.
     * </p>
     *
     * @return {@link Set <UserDto>} all the Users.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<UserDto> getAllUsers(int page, int size) throws CustomException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed user details for page : {}", page);
            return userPage.getContent().stream()
                    .map(UserMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all users", e);
            throw new CustomException("Server Error!!!!", e);
        }
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
    public UserDto getUserById(String id) throws NoSuchElementException, CustomException {
        try {
            User user = userRepository.findByIdAndIsDeletedFalse(id);
            if (user == null) {
                throw new NoSuchElementException("User not found for the given id: " + id);
            }
            logger.info("Retrieved user details for ID: {}", id);
            return UserMapper.modelToDto(user);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("User not found", e);
                throw e;
            }
            logger.error("Error retrieving an user", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves an user model.
     * </p>
     *
     * @param id of a user.
     * @return the User model
     */
    public User getUserModelById(String id) {
        try {
            User user = userRepository.findByIdAndIsDeletedFalse(id);
            logger.info("Retrieved user for the given id: {}", id);
            return user;
        } catch (Exception e) {
            logger.error("Error in retrieving user for the given id: {}", id);
            throw new CustomException("Server error!!", e);
        }
    }

    public Set<UserDto> getUsersByRole(String name) {
        try {
            Role role = roleService.getRoleByName(name);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given name: " + name);
            }
            return userRepository.findByRoleId(role.getId()).stream()
                    .map(UserMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn(e);
                throw e;
            }
            logger.error("Error in retrieving a role : {}", name, e);
            throw new CustomException("Server error!!", e);
        }
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
        try {
            User user = userRepository.findByIdAndIsDeletedFalse(id);
            if (user == null) {
                throw new NoSuchElementException("User not found for the given id: " + id);
            }
            Set<Role> roles = user.getRole();
            if (roles != null && !roles.isEmpty()) {
                user.setRole(null);
            }
            user.setIsDeleted(true);
            userRepository.save(user);
            logger.info("User removed successfully with ID: {}", id);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Error in removing a user : {}", id, e);
                throw e;
            }
            logger.error("Error removing an user", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Login an user based on the provided Username and password. Generates new token for a user.
     * </p>
     *
     * @param userDTO object.
     * @return the created token as string.
     * @throws CustomException .
     */
    public String authenticateUser(UserDto userDTO) throws CustomException {
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

    public Set<ReviewDto> getReviewsByUserId(String userId, int page, int size) {
        return reviewService.getAllReviewsByUserId(userId, page, size);
    }

    private User initializeUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setCreatedBy(user.getId());
        user.setIsDeleted(false);
        return user;
    }
}

