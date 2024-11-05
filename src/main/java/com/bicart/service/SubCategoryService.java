package com.bicart.service;

import com.bicart.dto.SubCategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.SubCategoryMapper;
import com.bicart.model.Category;
import com.bicart.model.SubCategory;
import com.bicart.repository.SubCategoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryService categoryService;

    private static final Logger logger = LogManager.getLogger(SubCategoryService.class);

    public SubCategory getSubCategoryByName(@NonNull String subCategoryName) {
        try {
            SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryName);
            if (subCategory == null) {
                throw new NoSuchElementException("Sub category not found for the Name: " + subCategoryName);
            }
            return subCategory;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Sub category not found for the Name: {} ", subCategoryName, e);
                throw e;
            }
            logger.error("Error in retrieving the subcategory: {}", subCategoryName, e);
            throw new CustomException("Error while fetching sub category");
        }
    }

    public Set<SubCategoryDto> getSubCategories(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return subCategoryRepository.findAllByIsDeletedFalse(pageable).getContent().stream()
                    .map(SubCategoryMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the sub categories", e);
            throw new CustomException("Error while fetching sub categories");
        }
    }

    public SubCategory saveSubCategory(@NonNull SubCategory subCategory) {
        try {
            return subCategoryRepository.save(subCategory);
        } catch (Exception e) {
            logger.error("Error in saving the sub category ", e);
            throw new CustomException("Error while saving sub category");
        }
    }

    public SubCategoryDto addSubCategory(@NonNull SubCategoryDto subCategoryDto) {
        try {
            if (subCategoryRepository.existsByName(subCategoryDto.getName())) {
                throw new DuplicateKeyException("Sub category with name " + subCategoryDto.getName() + " already exists");
            }
            SubCategory subCategory = saveSubCategory(SubCategoryMapper.dtoToModel(subCategoryDto));
            return SubCategoryMapper.modelToDto(subCategory);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                logger.error("Sub category with name " + subCategoryDto.getName() + " already exists", e);
                throw e;
            }
            logger.error("error in adding sub category : {}", subCategoryDto.getName(), e);
            throw new CustomException("Error while adding sub category");
        }
    }

    public SubCategoryDto updateSubCategory(@NonNull SubCategoryDto subCategoryDto) {
        try {
            SubCategory subCategory = subCategoryRepository.findByNameAndIsDeletedFalse(subCategoryDto.getName());
            if (subCategory == null) {
                throw new NoSuchElementException("Sub category " + subCategoryDto.getName() + " not found");
            }
            subCategory.setDescription(subCategoryDto.getDescription());
            subCategory.setCategory(categoryService.getCategoryByName(subCategoryDto.getCategory().getName()));
            return SubCategoryMapper.modelToDto(saveSubCategory(subCategory));
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Sub category or Category not found", e);
                throw e;
            }
<<<<<<< HEAD
            logger.error("Error in updating category: {} ", categoryName, e);
            throw new CustomException("Error while updating category");
=======
            logger.error(e);
            throw new CustomException("Error while updating sub category");
>>>>>>> ae649a1 (FEAT: Added controllers)
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
                logger.warn("Sub category " + subCategoryName + " not found", e);
                throw e;
            }
            logger.error("Error in deleting Sub category: {}", subCategoryName, e);
            throw new CustomException("Error while deleting sub category");
        }
    }
}
