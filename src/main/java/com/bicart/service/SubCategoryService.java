package com.bicart.service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.SubCategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.SubCategoryMapper;
import com.bicart.model.Category;
import com.bicart.model.SubCategory;
import com.bicart.repository.SubCategoryRepository;

/**
 * <p>
 *   Service class that handles business logic related to sub categories.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryService categoryService;

    private static final Logger logger = LogManager.getLogger(SubCategoryService.class);

    /**
     * <p>
     * Saves a sub category.
     * </p>
     *
     * @param subCategory the sub category to save
     * @throws CustomException if an error occurs while saving the sub category
     */
    public void saveSubCategory(@NonNull SubCategory subCategory) {
        try {
            subCategoryRepository.save(subCategory);
        } catch (Exception e) {
            logger.error("Error in saving the sub category ", e);
            throw new CustomException("Cannot save sub category with name " + subCategory.getName());
        }
    }

    /**
     * <p>
     *   Fetches a sub category by name.
     * </p>
     *
     * @param subCategoryName the name of the sub category to fetch
     * @return {@link SubCategory} the sub category with the given name
     * @throws NoSuchElementException if the sub category is not found
     * @throws CustomException if an error occurs while fetching the sub category
     */
    public SubCategoryDto getSubCategoryByName(@NonNull String subCategoryName) {
        SubCategory subCategory = getSubCategoryModelByName(subCategoryName);
        return SubCategoryMapper.modelToDto(subCategory);
    }

    /**
     * <p>
     *   Fetches a sub category model by name.
     * </p>
     *
     * @param subCategoryName the name of the sub category to fetch
     * @return {@link SubCategory} the sub category model with the given name
     * @throws NoSuchElementException if the sub category is not found
     */
    public SubCategory getSubCategoryModelByName(String subCategoryName) {
        SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryName);
        if (subCategory == null) {
            logger.warn("Sub category not found for the Name: {} ", subCategoryName);
            throw new NoSuchElementException("Sub category not found for the Name: " + subCategoryName);
        }
        return subCategory;
    }

    /**
     * <p>
     *   Fetches all sub categories.
     * </p>
     *
     * @param page the page number
     * @param size the number of sub categories per page
     * @return {@link Set<SubCategoryDto>} the set of sub categories on the given page
     * @throws CustomException if an error occurs while fetching the sub categories
     */
    public Set<SubCategoryDto> getSubCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return subCategoryRepository.findAllByIsDeletedFalse(pageable).getContent().stream()
                .map(SubCategoryMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Adds a sub category.
     * </p>
     *
     * @param subCategoryDto the sub category to add
     * @throws DuplicateKeyException if the sub category already exists
     * @throws NoSuchElementException if the category is not found
     * @throws CustomException if an error occurs while adding the sub category
     */
    public void addSubCategory(@NonNull SubCategoryDto subCategoryDto) {
        if (subCategoryRepository.existsByName(subCategoryDto.getName())) {
            logger.warn("Sub category with name {} already exists", subCategoryDto.getName());
            throw new DuplicateKeyException("Sub category with name " + subCategoryDto.getName() + " already exists");
        }
        SubCategory subCategory = SubCategoryMapper.dtoToModel(subCategoryDto);
        Category category = categoryService.getCategoryModelByName(subCategoryDto.getCategory().getName());
        subCategory.setCategory(category);
        subCategory.setId(UUID.randomUUID().toString());
        subCategory.setAudit("ADMIN");
        saveSubCategory(subCategory);
        logger.info("Sub category {} added successfully", subCategoryDto.getName());
    }

    /**
     * <p>
     * Updates a sub category.
     * </p>
     *
     * @param subCategoryDto the sub category to update
     * @throws NoSuchElementException if the sub category is not found
     * @throws CustomException        if an error occurs while updating the sub category
     */
    public SubCategoryDto updateSubCategory(@NonNull SubCategoryDto subCategoryDto) {
        SubCategory subCategory = getSubCategoryModelByName(subCategoryDto.getName());
        subCategory.setDescription(subCategoryDto.getDescription());
        Category category = categoryService.getCategoryModelByName(subCategoryDto.getCategory().getName());
        subCategory.setCategory(category);
        saveSubCategory(subCategory);
        logger.info("Sub category {} updated successfully", subCategoryDto.getName());
        return SubCategoryMapper.modelToDto(subCategory);
    }

    /**
     * <p>
     *   Deletes a sub category.
     * </p>
     *
     * @param subCategoryName the name of the sub category to delete
     * @throws NoSuchElementException if the sub category is not found
     * @throws CustomException if an error occurs while deleting the sub category
     */
    public void deleteSubCategory(@NonNull String subCategoryName) {
        SubCategory subCategory = getSubCategoryModelByName(subCategoryName);
        subCategory.setIsDeleted(true);
        saveSubCategory(subCategory);
        logger.info("Sub category {} deleted successfully", subCategoryName);
    }
}
