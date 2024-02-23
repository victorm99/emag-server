package com.emag.model.repository;

import com.emag.model.pojo.Category;
import com.emag.model.pojo.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    SubCategory findBySubcategoryName(String subcategoryName);
    List<SubCategory> findAllByCategoryId(long id);
}
