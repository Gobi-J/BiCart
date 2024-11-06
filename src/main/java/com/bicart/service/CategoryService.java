package com.bicart.service;

import com.bicart.dto.CategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CategoryMapper;
import com.bicart.model.Category;
import com.bicart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Service class that handles business logic related to category
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    /**
     * <p>
     *     Fetches category by name
     * </p>
     * @param categoryName category to be fetched
     * @return {@link Category} object which is requested
     * @throws NoSuchElementException if category is not found
     * @throws CustomException if any other error occurs
     */
    public Category getCategoryByName(String categoryName) {
        try {
            Category category = categoryRepository.findByNameAndIsDeletedFalse(categoryName);
            if (category == null) {
                throw new NoSuchElementException("Category not found for the name : {}" + categoryName);
            }
            return category;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Category not found for the Name: {}", categoryName, e);
                throw e;
            }
            logger.error("Error in retrieving the category with the name: {}", categoryName, e);
            throw new CustomException("Error while fetching category");
        }
    }

    /**
     * <p>
     *     Fetches all categories
     * </p>
     * @param page page number
     * @param size number of categories to be fetched
     * @return {@link CategoryDto} objects
     * @throws CustomException if any error occurs
     */
    public Set<CategoryDto> getAllCategories(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            logger.info("Retrieved all Categories for the page : {}", page);
            return categoryRepository.findAllByIsDeletedFalse(pageable).stream()
                    .map(CategoryMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving categories for the page : {}", page, e);
            throw new CustomException("Error while fetching categories");
        }
    }

    /**
     * <p>
     *     Saves category
     * </p>
     * @param category category to be saved
     * @return saved {@link Category} object
     * @throws CustomException if any error occurs
     */
    public Category saveCategory(Category category) {
        logger.debug("Saving Category with the id : {}", category.getId());
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            logger.error("Error in saving the category with the id : {}", category.getId(), e);
            throw new CustomException("Error while saving category");
        }
    }

    /**
     * <p>
     *     Adds category
     * </p>
     * @param categoryDto category to be added
     * @return added {@link CategoryDto} object
     * @throws CustomException if any error occurs
     */
    public CategoryDto addCategory(CategoryDto categoryDto) {
        try {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new DuplicateKeyException("Category with name " + categoryDto.getName() + " already exists");
            }

            Category category = CategoryMapper.dtoToModel(categoryDto);
            category.setId(UUID.randomUUID().toString());
            category.setCreatedAt(new Date());
            category.setIsDeleted(false);
            category = saveCategory(category);
            logger.info("Saving the Category with the id : {}", category.getId());
            return CategoryMapper.modelToDto(category);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                logger.error("Category with name " + categoryDto.getName() + " already exists");
                throw e;
            }
            logger.error("Error in adding the category with the Id : {}", categoryDto.getId(), e);
            throw new CustomException("Error while adding category");
        }
    }

    /**
     * <p>
     *     Updates category
     * </p>
     * @param newCategory category to be updated
     * @return updated {@link CategoryDto} object
     * @throws CustomException if any error occurs
     */
    public CategoryDto updateCategory(CategoryDto newCategory) {
        try {
            Category category = CategoryMapper.dtoToModel(newCategory);
            category.setId(getCategoryByName(newCategory.getName()).getId());
            logger.info("Updated Category with the Name: {}", category.getName());
            return CategoryMapper.modelToDto(saveCategory(category));
        } catch (Exception e) {
            logger.error("Error in updating the category with the Name: {}", newCategory.getName(), e);
            throw new CustomException("Error while updating category");
        }
    }

    /**
     * <p>
     *     Deletes category
     * </p>
     * @param categoryName category to be deleted
     * @throws NoSuchElementException if category is not found
     * @throws CustomException if any other error occurs
     */
    public void deleteCategory(String categoryName) {
        try {
            Category category = getCategoryByName(categoryName);
            if (category == null) {
                throw new NoSuchElementException("Category not found");
            }
            category.setIsDeleted(true);
            saveCategory(category);
            logger.info("Deleted Category with the Name: {}", categoryName);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Category with the name " + categoryName + " already exists");
                throw e;
            }
            logger.error("Error in deleting the category with the name : {}", categoryName, e);
            throw new CustomException("Error while deleting category");
        }
    }
}
