package com.bicart.service;

import com.bicart.dto.RoleDto;
import com.bicart.dto.UserDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.RoleMapper;
import com.bicart.mapper.UserMapper;
import com.bicart.model.Role;
import com.bicart.repository.RoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

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
            roleRepository.save(role);
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
    public Set<RoleDto> getAllRoles(int page, int size) throws CustomException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Role> rolePage = roleRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed role details for page : {}", page);
            return rolePage.getContent().stream()
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
     * @param id the ID of the role whose details are to be viewed
     * @return the Role object.
     * @throws NoSuchElementException when occurred.
     */
    public Role getRoleById(String id) throws NoSuchElementException, CustomException {
        try {
            Role role = roleRepository.findByIdAndIsDeletedFalse(id);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given id: " + id);
            }
            logger.info("Retrieved role details for ID: {}", id);
            return role;
        } catch (NoSuchElementException e) {
            logger.error("Role not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in retrieving a role : {}", id, e);
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Deletes the role with the given ID.
     * </p>
     *
     * @param id the ID of the role to be deleted.
     * @throws CustomException if exception is thrown.
     */
    public void deleteRole(String id) throws CustomException {
        try {
            Role role = roleRepository.findByIdAndIsDeletedFalse(id);
            if (role == null) {
                throw new NoSuchElementException("Role not found for the given id: " + id);
            }
            role.setDeleted(true);
            roleRepository.save(role);
            logger.info("Role deleted successfully with ID: {}", id);
        } catch (NoSuchElementException e) {
            logger.error("Role not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in deleting a role : {}", id, e);
            throw new CustomException("Server error!!", e);
        }
    }
}







