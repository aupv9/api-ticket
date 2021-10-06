package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Theater;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.TheaterRepository;
import com.apps.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    ApplicationCacheManager cacheManager;

    @Override
    @Cacheable(value = "TheaterService" ,key = "'TheaterList_'+#page +'-'+#size +'-'+#sort +'-'+#order +'-'+#search+'-'+#location" , unless = "#result == null")
    public  List<Theater> findAll(Integer page, Integer size, String sort,String order,
                                  String search, Integer location) {
            return this.theaterRepository.findAll(size,page*size,sort,order,search,location);
    }

    @Override
    public int insert(Theater theater) {
        int result = this.theaterRepository.insert(theater);
        cacheManager.clearCache("TheaterService");
        return result;
    }

    @Override
    @Cacheable(value = "TheaterService" ,key = "'findById_Theater_'+#id" , unless = "#result == null")
    public Theater findById(Integer id) {
        Theater theater = this.theaterRepository.findById(id);
        if(theater == null){
            throw new NotFoundException("Not Found Object have Id:"+id);
        }
        return theater;
    }

    @Override
    public int update(Theater theater) {
        Theater theater1 = this.findById(theater.getId());
            theater1.setThumbnail(theater.getThumbnail());
            theater1.setImage(theater.getImage());
            theater1.setCode(theater.getCode());
            theater1.setName(theater.getName());
            theater1.setLatitude(theater.getLatitude());
            theater1.setLongitude(theater.getLongitude());
            theater1.setLocationId(theater.getLocationId());
        int result = this.theaterRepository.update(theater1);
        cacheManager.clearCache("TheaterService");
        return result;
    }

    @Override
    @CacheEvict(cacheNames = "TheaterService",key = "'findById_Theater_'+#id" )
    public void deleteById(Integer id) {
        Theater theater = this.findById(id);
        this.theaterRepository.delete(theater.getId());
    }

    @Override
    public Theater findByLocation(Integer id) {
        return this.theaterRepository.findByLocation(id);
    }

    @Override
    public int findCountAll(String search, Integer location) {
        return this.theaterRepository.findCountAll(search,location);
    }

}
