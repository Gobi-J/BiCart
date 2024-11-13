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
     * @param roleDTO to create new role.
     * @throws CustomException if exception is thrown.
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
     * Retrieves and displays all role.
     * </p>
     *
     * @return {@link Set <RoleDto>} all the Role.
     * @throws CustomException, when any custom Exception is thrown.
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
     * @return the Role object.
     * @throws NoSuchElementException when occurred.
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
     * @return the Role object.
     * @throws NoSuchElementException when occurred.
     */
    public Role getRoleModelByName(String name) {
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
     * @throws CustomException if exception is thrown.
     */
    public void deleteRole(String name) {
        Role role = getRoleModelByName(name);
        role.setIsDeleted(true);
        saveRole(role);
        logger.info("{} role deleted successfully", name);
    }
}







