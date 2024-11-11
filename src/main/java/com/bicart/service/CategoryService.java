package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.Set;
import java.util.stream.Collectors;

import com.bicart.model.Product;
import com.bicart.model.SubCategory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.CategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CategoryMapper;
import com.bicart.model.Category;
import com.bicart.repository.CategoryRepository;

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
   // private final SubCategoryService subCategoryService;

    /**
     * <p>
     * Saves category
     * </p>
     *
     * @param category category to be saved
     * @throws CustomException if any error occurs
     */
    public void saveCategory(Category category) {
        logger.debug("Saving Category with the id : {}", category.getId());
        try {
            categoryRepository.save(category);
            logger.info("Saved Category with the id : {}", category.getId());
        } catch (Exception e) {
            logger.error("Error in saving the category with the id : {}", category.getId(), e);
            throw new CustomException("Error while saving category");
        }
    }

    /**
     * <p>
     * Fetches category by name
     * </p>
     *
     * @param categoryName category to be fetched
     * @return {@link CategoryDto} object which is requested
     * @throws NoSuchElementException if category is not found
     */
    public CategoryDto getCategoryByName(String categoryName) {
        Category category = getCategoryModelByName(categoryName);
        return CategoryMapper.modelToDto(category);
    }

    /**
     * <p>
     * Fetches category by name
     * </p>
     *
     * @param categoryName category to be fetched
     * @return {@link Category} object which is requested
     * @throws NoSuchElementException if category is not found
     * @throws CustomException        if any other error occurs
     */
    public Category getCategoryModelByName(String categoryName) {
        Category category = categoryRepository.findByNameAndIsDeletedFalse(categoryName);
        if (category == null) {
            throw new NoSuchElementException("Category not found for the name : {}" + categoryName);
        }
        return category;
    }

    /**
     * <p>
     * Fetches all categories
     * </p>
     *
     * @param page page number
     * @param size number of categories to be fetched
     * @return {@link CategoryDto} objects
     * @throws CustomException if any error occurs
     */
    public Set<CategoryDto> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        logger.info("Retrieved all Categories for the page : {}", page);
        return categoryRepository.findAllByIsDeletedFalse(pageable).stream()
                .map(CategoryMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Adds category
     * </p>
     *
     * @param categoryDto category to be added
     * @throws CustomException if any error occurs
     */
    public void addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new DuplicateKeyException("Category with name " + categoryDto.getName() + " already exists");
        }
        Category category = CategoryMapper.dtoToModel(categoryDto);
        category.setId(UUID.randomUUID().toString());
        category.setAudit("ADMIN");
        saveCategory(category);
        logger.info("Saving the Category with the id : {}", category.getId());
    }

    /**
     * <p>
     * Updates category
     * </p>
     *
     * @param newCategory category to be updated
     * @return updated {@link CategoryDto} object
     * @throws CustomException if any error occurs
     */
    public CategoryDto updateCategory(CategoryDto newCategory) {
        Category category = CategoryMapper.dtoToModel(newCategory);
        category.setId(getCategoryByName(newCategory.getName()).getId());
        logger.info("Updated Category with the Name: {}", category.getName());
        saveCategory(category);
        return CategoryMapper.modelToDto(category);
    }

    /**
     * <p>
     * Deletes category
     * </p>
     *
     * @param categoryName category to be deleted
     * @throws NoSuchElementException if category is not found
     * @throws CustomException        if any other error occurs
     */
    public void deleteCategory(String categoryName) {
        Category category = getCategoryModelByName(categoryName);
      //  List<SubCategory> subCategories = subCategoryService.getAllCategories(categoryName);
       // Category unknownCategory = categoryRepository.findByName("Unknown");
//        if(unknownCategory == null) {
//            unknownCategory =  Category.builder()
//                    .id(UUID.randomUUID().toString())
//                    .name("Unknown")
//                    .description("Unknown category")
//                    .build();
//            unknownCategory.setAudit("ADMIN");
//            saveCategory(unknownCategory);
//        }
//        for(SubCategory subCategory : subCategories) {
//            subCategory.setCategory(unknownCategory);
//            subCategoryService.saveSubCategory(subCategory);
//        }
        category.setIsDeleted(true);
        saveCategory(category);
        logger.info("Deleted Category with the Name: {}", categoryName);
    }

}
