package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Concession;
import com.apps.domain.entity.Seat;
import com.apps.domain.repository.ConcessionsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ConcessionRepository;
import com.apps.service.ConcessionsService;
import lombok.var;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConcessionsServiceImpl implements ConcessionsService {

    private final ConcessionRepository concessionRepository;
    private final ConcessionsCustomRepository concessionsCustomRepository;
    private final ApplicationCacheManager cacheManager;

    public ConcessionsServiceImpl(ConcessionRepository concessionRepository, ConcessionsCustomRepository concessionsCustomRepository, ApplicationCacheManager cacheManager) {
        this.concessionRepository = concessionRepository;
        this.concessionsCustomRepository = concessionsCustomRepository;
        this.cacheManager = cacheManager;
    }

    @Override
//    @Cacheable(cacheNames = "ConcessionsService", key = "'ConcessionsList_'+#page +'-'+#size+'-'+#sort +'-'+#order+'-'+#name+'-'+#categoryId")
    public List<Concession> findAll(int limit, int offset, String sort, String order, String name, Integer categoryId) {
        return this.concessionRepository.findAll(limit, offset,sort,order,name,categoryId > 0 ? categoryId : null);
    }

    @Override
//    @Cacheable(cacheNames = "ConcessionsService", key = "'findCountAllConcessionsList_'+#name +'-'+#categoryId")
    public int findCountAll(String name, Integer categoryId) {
        int result = 0;
        try{
            result = this.concessionRepository.findCountAll(name,categoryId);
        }catch (NullPointerException nullPointerException){
            result = this.concessionRepository.findCountAll(name,null);
        }
        return result;
    }

    @Override
    @Cacheable(cacheNames = "ConcessionsService", key = "'findByIdConcessions_'+#id")
    public Concession findById(Integer id) {
        Concession concession = this.concessionRepository.findById(id);
        if(concession == null){
            throw new NotFoundException("Not Found Object have Id:" + id);
        }
        return concession;
    }

    @Override
    public int update(Concession concession) {
        Concession concession1 = this.concessionRepository.findById(concession.getId());
        concession1.setName(concession.getName());
//        concessions1.setPrice(concessions.getPrice());
//        concessions1.setCategoryId(concessions.getCategoryId());
        int result = this.concessionRepository.update(concession1);
        cacheManager.clearCache("ConcessionsService");
        return result;
    }

    @Override
    public void delete(Integer id) {
        Concession concession = this.concessionRepository.findById(id);
        this.concessionRepository.delete(concession.getId());
        cacheManager.clearCache("ConcessionsService");
    }

    @Override
    public int insert(Concession concession) throws SQLException {
        String sql = "Insert into concession(name,price,category_id,image,thumbnail) values(?,?,?,?,?)";
        int id = this.concessionsCustomRepository.insert(concession,sql);
        cacheManager.clearCache("ConcessionsService");
        return id;
    }

    @Override
    public List<Concession> findAll() {
//        Map<Integer,List<Concession>> map = new HashMap<>();
//        for (var item:concessionList){
//            var key = item.getCategoryId();
//            if(!map.containsKey(key)) {
//                map.put(key, new ArrayList<>());
//                map.get(key).add(item);
//            }else{
//                map.get(key).add(item);
//            }
//        }
//        var listResult = new ArrayList<List<Concession>>();
//        for (var item: map.entrySet()){
//            listResult.add(item.getValue());
//        }
        return  this.concessionRepository.findAllConcession();
    }
}
