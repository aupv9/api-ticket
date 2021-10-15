package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Category;
import com.apps.domain.entity.Concessions;
import com.apps.domain.repository.ConcessionsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ConcessionRepository;
import com.apps.mybatis.mysql.ConcessionsRepository;
import com.apps.service.ConcessionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ConcessionsServiceImpl implements ConcessionsService {

//    private final FoodRepository foodsRepository;
//    private final FoodsCustomRepository foodsCustomRepository;
//    private final ApplicationCacheManager cacheManager;
//
//    public FoodsServiceImpl(FoodRepository foodsRepository, FoodsCustomRepository foodsCustomRepository, ApplicationCacheManager cacheManager) {
//        this.foodsRepository = foodsRepository;
//        this.foodsCustomRepository = foodsCustomRepository;
//        this.cacheManager = cacheManager;
//    }
    @Autowired
    private ConcessionsRepository concessionsRepository;
    @Autowired
    private ConcessionsCustomRepository concessionsCustomRepository;
    @Autowired
    private  ApplicationCacheManager cacheManager;

    @Autowired
    private ConcessionRepository concessionRepository;

    @Override
    @Cacheable(cacheNames = "FoodsService", key = "'FoodsList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#name +'-'+#price+'-'+#categoryId")
    public List<Category> findAll(int page, int size, String sort, String order, String name, double price, int categoryId) {
        return this.concessionsRepository.findAll(size, page * size,sort,order,name,price,categoryId);
    }

    @Override
    @Cacheable(cacheNames = "FoodsService", key = "'findCountAllFoodsList_'+#name +'-'+#categoryId")
    public int findCountAll(String name, int categoryId) {
        return this.concessionsRepository.findCountAll(name,categoryId);
    }

    @Override
    @Cacheable(cacheNames = "FoodsService", key = "'findByIdFoods_'+#id")
    public Concessions findById(Integer id) {
        Concessions concessions = this.concessionsRepository.findById(id);
        if(concessions == null){
            throw new NotFoundException("Not Found Object have Id:" + id);
        }
        return concessions;
    }

    @Override
    public int update(Concessions concessions) {
        Concessions concessions1 = this.concessionsRepository.findById(concessions.getId());
        concessions1.setName(concessions.getName());
        concessions1.setPrice(concessions.getPrice());
        concessions1.setCategoryId(concessions.getCategoryId());
        int result = this.concessionsRepository.update(concessions1);
        cacheManager.clearCache("FoodsService");
        return result;
    }

    @Override
    public void delete(Integer id) {
        Concessions concessions = this.concessionsRepository.findById(id);
        this.concessionsRepository.delete(concessions.getId());
        cacheManager.clearCache("FoodsService");
    }

    @Override
    public int insert(Concessions concessions) throws SQLException {
        String sql = "Insert into concession(name,price,category_id) values(?,?,?)";
        int id = this.concessionsCustomRepository.insert(concessions,sql);
        cacheManager.clearCache("FoodsService");
        return id;
    }
}
