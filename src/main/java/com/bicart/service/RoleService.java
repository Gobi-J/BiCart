package com.bicart.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.dto.RoleDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.RoleMapper;
import com.bicart.model.Role;
import com.bicart.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private static final Logger logger = LogManager.getLogger(RoleService.class);

    /**
     * <p>
     * Saves a Role.
     * </p>
     *
     * @param role model.
     */
    public void saveRole(Role role) {
        try {
            roleRepository.save(role);
            logger.info("Role saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving role");
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Creates a new Role object and saves it in the repository.
     * </p>
     *
     * @param roleDTO to create new role.
     * @return the created roleDto object.
     * @throws CustomException if exception is thrown.
     */
    public RoleDto addRole(RoleDto roleDTO) throws CustomException {
        try {
            Role role = RoleMapper.dtoToModel((roleDTO));
            role.setId(UUID.randomUUID().toString());
            role.setIsDeleted(false);
            role.setCreatedAt(new Date());
            saveRole(role);
            RoleDto roleDto = RoleMapper.modelToDto((role));
            logger.info("Role added successfully with ID: {}", roleDto.getId());
            return roleDto;
        } catch (Exception e) {
            logger.error("Error adding a role", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all role.
     * </p>
     *
     * @return {@link Set <RoleDto>} all the Role.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<RoleDto> getAllRoles() throws CustomException {
        try {
            List<Role> roles = roleRepository.findAllByIsDeletedFalse();
            logger.info("Displayed role details for page");
            return roles.stream()
                    .map(RoleMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all roles", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays the details of an roles.
     * </p>
     *
     * @param name the name of the role whose details are to be viewed
     * @return the Role object.
     * @throws NoSuchElementException when occurred.
     */
    public Role getRoleByName(String name) throws NoSuchElementException, CustomException {
        try {
            Role role = roleRepository.findByRoleNameAndIsDeletedFalse(name);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given name: " + name);
            }
            logger.info("Retrieved role details for name: {}", name);
            return role;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Role not found", e);
                throw e;
            }
            logger.error("Error in retrieving a role : {}", name, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Deletes the role with the given ID.
     * </p>
     *
     * @param name the name of the role to be deleted.
     * @throws CustomException if exception is thrown.
     */
    public void deleteRole(String name) throws CustomException {
        try {
            Role role = roleRepository.findByRoleNameAndIsDeletedFalse(name);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given name: " + name);
            }
            role.setIsDeleted(true);
            saveRole(role);
            logger.info("Role deleted successfully with ID: {}", name);
        } catch (NoSuchElementException e) {
            logger.error("Role not found for the given name: {} ", name, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in deleting a role : {}", name, e);
            throw new CustomException("Server error!!", e);
        }
    }
}







