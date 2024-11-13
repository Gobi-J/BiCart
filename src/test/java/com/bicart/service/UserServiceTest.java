package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.bicart.dto.UserDto;
import com.bicart.dto.UserRoleDto;
import com.bicart.helper.UnAuthorizedException;
import com.bicart.model.Role;
import com.bicart.model.User;
import com.bicart.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private User user;
    private UserDto userDto;
    private Role role;
    private Set<Role> roles;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("1234")
                .name("John Doe")
                .email("john@gmail.com")
                .mobileNumber("1234567890")
                .role(roles)
                .build();

        userDto = UserDto.builder()
                .id("1234")
                .name("John Doe")
                .email("john@gmail.com")
                .mobileNumber("1234567890")
                .password("password")
                .build();

        roles = Set.of(Role.builder()
                .id("1234")
                .roleName("ADMIN").build());

        role = Role.builder()
                .id("1234")
                .roleName("USER")
                .build();
    }

    @Test
    void testAddUserSuccess() {
        when(userRepository.existsByEmailOrMobileNumber(anyString(), anyString())).thenReturn(false);
        when(roleService.getRoleModelByName("USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.addUser(userDto);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAddUserNotExists() {
        when(userRepository.existsByEmailOrMobileNumber(anyString(), anyString())).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> userService.addUser(userDto));
    }

    @Test
    void testUpdateUserSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertInstanceOf(UserDto.class, userService.updateUser(userDto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsersSuccess() {
        when(userRepository.findAllByIsDeletedFalse(PageRequest.of(0, 1))).thenReturn(List.of(user));
        assertEquals(1, userService.getAllUsers(0, 1).size());
    }

    @Test
    void testGetUserByIdPresent() {
        when(userRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(user);
        UserRoleDto result = userService.getUser("1234");
        assertEquals(result.getName(), user.getName());
    }

    @Test
    void testGetUserByIdFailure() {
        when(userRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> userService.getUser("1234"));
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.deleteUser("1234");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserNotExists() {
        when(userRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser("1234"));
    }

    @Test
    void testAuthenticateUserSuccess() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        assertInstanceOf(String.class, userService.authenticateUser(userDto));
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testAuthenticateUserFailure() {
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        assertThrows(UnAuthorizedException.class, () -> userService.authenticateUser(userDto));
        verify(authenticationManager, times(1)).authenticate(any());
    }

//    @Test
//    void testMakeAdminSuccess() {
//        when(userRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(user);
//        when(roleService.getRoleModelByName("ADMIN")).thenReturn(role);
//        when(userRepository.save(any(User.class))).thenReturn(user);
//        userService.makeAdmin(userDto);
//        verify(userRepository, times(1)).save(any(User.class));
//    }
}