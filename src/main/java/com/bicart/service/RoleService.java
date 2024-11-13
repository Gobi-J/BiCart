package com.bicart.service;

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

/**
 * <p>
 * Service class that handles business logic related to roles.
 * </p>
 */
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
     * @param role details to save
     * @throws CustomException if any issues occur while saving the role.
     */
    private void saveRole(Role role) {
        try {
            roleRepository.save(role);
            logger.info("Role saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving role");
            throw new CustomException("Cannot save role");
        }
    }

    /**
     * <p>
     * Creates a new Role object and saves it in the repository.
     * </p>
     *
     * @param roleDTO to create a new role.
     */
    public void addRole(RoleDto roleDTO) {
        Role role = RoleMapper.dtoToModel(roleDTO);
        role.setId(UUID.randomUUID().toString());
        role.setAudit("ADMIN");
        saveRole(role);
        logger.info("{} role added successfully", role.getRoleName());
    }

    /**
     * <p>
     * Retrieves details of all roles.
     * </p>
     *
     * @return {@link RoleDto} set containing all the information.
     */
    public Set<RoleDto> getAllRoles() throws CustomException {
        List<Role> roles = roleRepository.findAllByIsDeletedFalse();
        logger.info("Displayed role details for page");
        return roles.stream()
                .map(RoleMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves and displays the details of an roles.
     * </p>
     *
     * @param name the name of the role whose details are to be viewed
     * @return {@link RoleDto} of the role requested.
     */
    public RoleDto getRoleByName(String name) {
        Role role = getRoleModelByName(name);
        return RoleMapper.modelToDto(role);
    }

    /**
     * <p>
     * Retrieves and displays the details of an roles.
     * </p>
     *
     * @param name the name of the role whose details are to be viewed
     * @return {@link Role} of the role requested.
     * @throws NoSuchElementException if the role is not found.
     */
    protected Role getRoleModelByName(String name) {
        Role role = roleRepository.findByRoleNameAndIsDeletedFalse(name);
        if (role == null) {
            throw new NoSuchElementException("Role not found for the given name: " + name);
        }
        logger.info("Retrieved role details for name: {}", name);
        return role;
    }

    /**
     * <p>
     * Deletes the role with the given name.
     * </p>
     *
     * @param name the name of the role to be deleted.
     */
    public void deleteRole(String name) {
        Role role = getRoleModelByName(name);
        role.setIsDeleted(true);
        saveRole(role);
        logger.info("{} role deleted successfully", name);
    }
}







