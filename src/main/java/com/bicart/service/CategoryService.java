package com.bicart.service;

import com.bicart.dto.CategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CategoryMapper;
import com.bicart.model.Category;
import com.bicart.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    public Category getCategoryByName(String categoryName) {
        try {
            Category category = categoryRepository.findByNameAndIsDeletedFalse(categoryName);
            if (category == null) {
                throw new NoSuchElementException("Category not found");
            }
            return category;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn(e);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while fetching category");
        }
    }

    public List<CategoryDto> getAllCategories(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return categoryRepository.findAllByIsDeletedFalse(pageable).stream()
                    .map(CategoryMapper::modelToDto)
                    .toList();
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while fetching categories");
        }
    }

    public void saveCategory(Category category) {
        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while saving category");
        }
    }

    public void addCategory(CategoryDto category) {
        try {
            if (categoryRepository.existsByName(category.getName())) {
                throw new CustomException("Category with name " + category.getName() + " already exists");
            }
            saveCategory(CategoryMapper.dtoToModel(category));
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while adding category");
        }
    }

    public void updateCategory(CategoryDto newCategory) {
        try {
            Category category = CategoryMapper.dtoToModel(newCategory);
            category.setId(getCategoryByName(newCategory.getName()).getId());
            saveCategory(category);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while updating category");
        }
    }

    public void deleteCategory(String categoryName) {
        try {
            Category category = getCategoryByName(categoryName);
            if (category == null) {
                throw new NoSuchElementException("Category not found");
            }
            category.setDeleted(true);
            saveCategory(category);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while deleting category");
        }
    }
}
