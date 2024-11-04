package com.bicart.service;

import com.bicart.dto.SubCategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.SubCategoryMapper;
import com.bicart.model.Category;
import com.bicart.model.SubCategory;
import com.bicart.repository.SubCategoryRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    private static final Logger logger = LogManager.getLogger(SubCategoryService.class);

    public SubCategory getSubCategoryByName(@NonNull String subCategoryName) {
        try {
            SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryName);
            if (subCategory == null) {
                throw new NoSuchElementException("Sub category not found");
            }
            return subCategory;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn(e);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while fetching sub category");
        }
    }

    public SubCategory getSubCategories(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return subCategoryRepository.findAllByIsDeletedFalse(pageable).stream()
                    .map(SubCategoryMapper::modelToDto)
                    .toList();
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while fetching sub categories");
        }
    }

    public SubCategory saveSubCategory(@NonNull SubCategory subCategory) {
        try {
            return subCategoryRepository.save(subCategory);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while saving sub category");
        }
    }

    public SubCategoryDto addSubCategory(@NonNull SubCategoryDto subCategoryDto) {
        try {
            if (subCategoryRepository.existsByName(subCategoryDto.getName())) {
                throw new CustomException("Sub category with name " + subCategoryDto.getName() + " already exists");
            }
            SubCategory subCategory = saveSubCategory(SubCategoryMapper.dtoToModel(subCategoryDto));
            return SubCategoryMapper.modelToDto(subCategory);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while adding sub category");
        }
    }

    public void updateCategory(@NonNull String subCategoryName, @NonNull String categoryName) {
        try {
            SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryName);
            if (subCategory == null) {
                throw new NoSuchElementException("Sub category " + subCategoryName + " not found");
            }
            Category category = categoryService.getCategoryByName(categoryName);
            if (category == null) {
                throw new NoSuchElementException("Category " + categoryName + " not found");
            }
            subCategory.setCategory(category);
            saveSubCategory(subCategory);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn(e);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while updating category");
        }
    }

    public void deleteSubCategory(@NonNull String subCategoryName) {
        try {
            SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryName);
            if (subCategory == null) {
                throw new NoSuchElementException("Sub category " + subCategoryName + " not found");
            }
            subCategory.setIsDeleted(true);
            saveSubCategory(subCategory);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn(e);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while deleting sub category");
        }
    }
}
