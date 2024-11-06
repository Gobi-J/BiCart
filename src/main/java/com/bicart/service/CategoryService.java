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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

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

    public Category saveCategory(Category category) {
        try {
            logger.info("Saving Category with the id : {}", category.getId());
            return categoryRepository.save(category);
        } catch (Exception e) {
            logger.error("Error in saving the category with the id : {}", category.getId(), e);
            throw new CustomException("Error while saving category");
        }
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        try {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new DuplicateKeyException("Category with name " + categoryDto.getName() + " already exists");
            }
            Category category = saveCategory(CategoryMapper.dtoToModel(categoryDto));
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
