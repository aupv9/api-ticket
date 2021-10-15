package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Category;
import com.apps.domain.repository.CategoryCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.CategoryRepository;
import com.apps.service.CategoryService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final ApplicationCacheManager cacheManager;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryCustomRepository categoryCustomRepository, ApplicationCacheManager cacheManager) {
        this.categoryRepository = categoryRepository;
        this.categoryCustomRepository = categoryCustomRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(cacheNames = "CategoryService", key = "'CategoryList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#name +'-'+#type")
    public List<Category> findAll(int page, int size, String sort, String order, String name, String type) {
        return this.categoryRepository.findAll(size,page * size,sort,order,name,type);
    }

    @Override
    @Cacheable(cacheNames = "CategoryService", key = "'findCountAllCategoryList_'+#name +'-'+#type")
    public int findCountAll(String name, String type) {
        return this.categoryRepository.findCountAll(name,type);
    }

    @Override
    @Cacheable(cacheNames = "CategoryService", key = "'findByIdCategory_'+#id")
    public Category findById(Integer id) {
        Category category = this.categoryRepository.findById(id);
        if(category == null){
            throw new NotFoundException("Not Found Object have Id:" + id);
        }
        return category;
    }

    @Override
    public int update(Category category) {
        Category category1 = this.findById(category.getId());
        category1.setName(category.getName());
        category1.setDescription(category.getDescription());
        category1.setType(category.getType());
        int result = this.categoryRepository.update(category1);
        this.cacheManager.clearCache("CategoryService");
        return result;
    }

    @Override
    @CacheEvict(cacheNames = "CategoryService", key = "'findByIdCategory_'+#id")
    public void delete(Integer id) {
        Category category = this.findById(id);
        this.cacheManager.clearCache("CategoryService");
        this.categoryRepository.delete(category.getId());
    }

    @Override
    public int insert(Category category) throws SQLException {
        String sql = "Insert into category(name,description,type) values(?,?,?)";
        int id = this.categoryCustomRepository.insert(category,sql);
        this.cacheManager.clearCache("CategoryService");
        return id;
    }
}
