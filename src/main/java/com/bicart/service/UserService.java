package com.bicart.service;

import com.bicart.dto.OrderDto;
import com.bicart.dto.ReviewDto;
import com.bicart.dto.UserDto;
import com.bicart.helper.CustomException;
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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AddressService addressService;

    private final ReviewService reviewService;

    private final ProductService productService;

    private AuthenticationManager authenticationManager;

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final RoleService roleService;

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
    public UserDto addUser(UserDto userDTO) {
        try {
            User user = UserMapper.dtoToModel((userDTO));
            if (userRepository.existsByEmailAndIsDeletedFalse(userDTO.getEmail())) {
                throw new DuplicateKeyException("User exists with same Email Id" + userDTO.getEmail());
            }
            user.setPassword(encoder.encode(userDTO.getPassword()));
            saveUser(user);
            UserDto userDto = UserMapper.modelToDto((user));
            logger.info("User added successfully with ID: {}", userDto.getId());
            return userDto;
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                logger.error("User already exists with same Email Id" + userDTO.getEmail(), e);
                throw e;
            }
            logger.error("Error adding an user with ID: {}", userDTO.getId(), e);
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
    public UserDto updateUser(UserDto userDto) {
        try {
            User user = UserMapper.dtoToModel((userDto));
            userRepository.save(user);
            logger.info("User updated successfully for ID: {}", userDto.getId());
            return UserMapper.modelToDto((user));
        } catch (Exception e) {
            logger.error("Error updating user for ID: {}", userDto.getId(), e);
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
    public Set<UserDto> getAllUsers(int page, int size) {
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
    public UserDto getUserById(String id) {
        try {
            User user = userRepository.findByIdAndIsDeletedFalse(id);
            if (user == null) {
                throw new NoSuchElementException("User not found for the given id: " + id);
            }
            logger.info("Retrieved user details for ID: {}", id);
            return UserMapper.modelToDto(user);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("User not found for the given id: " + id, e);
                throw e;
            }
            logger.error("Error retrieving an user for the given id: " + id, e);
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

    public Set<UserDto> getUsersByRole(String roleId) {
        try {
            Role role = roleService.getRoleById(roleId);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given id: " + roleId);
            }
            return userRepository.findByRole(role).stream()
                    .map(UserMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Role not found for the given id: " + roleId,e);
                throw e;
            }
            logger.error("Error in retrieving a role : {}", roleId, e);
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
    public void deleteUser(String id) {
        try {
            User user = userRepository.findByIdAndIsDeletedFalse(id);
            if (user == null) {
                throw new NoSuchElementException("User not found for the given id: " + id);
            }
            Set<Role> roles = user.getRoles();
            if (roles != null && !roles.isEmpty()) {
                user.setRoles(null);
            }
            user.setIsDeleted(true);
            userRepository.save(user);
            logger.info("User removed successfully with ID: {}", id);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("User not found for the given id: {}", id, e);
                throw e;
            }
            logger.error("Error removing an user with ID: {}", id, e);
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
            throw new CustomException("Invalid Username or Password", e);
        }
    }

    public void deleteAddressWithUserId(String userId, String addressId) {
        try {
            Address address = addressService.getAddressModelById(addressId);
            if (Objects.equals(address.getUser().getId(), userId)) {
                addressService.deleteAddressById(addressId);
            } else {
                throw new CustomException("Address not found for the given user");
            }
        } catch (Exception e) {
            logger.error("Error in address retrieving", e);
            throw e;
        }
    }

    public Set<ReviewDto> getReviewsByUserId(String userId, int page, int size) {
        try {
            return reviewService.getAllReviewsByUserId(userId, page, size);
        } catch (Exception e) {
            logger.error("Error in retrieving the reviews by user Id: {}", userId, e);
            throw new CustomException("Server error!!", e);
        }
    }

    public Set<OrderDto> getOrdersByUserId(String userId, int page, int size) {
        try {
            return orderService.getOrdersByUserId(userId, page, size);
        } catch (Exception e) {
            logger.error("Error in retrieving the orders by user Id: {}", userId, e);
            throw new CustomException("Server error!!", e);
        }
    }
}

