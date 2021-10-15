package com.apps.service.impl;

import com.apps.config.cache.ApplicationCacheManager;
import com.apps.domain.entity.Concession;
import com.apps.domain.repository.ConcessionsCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.mybatis.mysql.ConcessionRepository;
import com.apps.service.ConcessionsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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
    public List<Concession> findAll(int page, int size, String sort, String order, String name, int categoryId) {
        return this.concessionRepository.findAll(size, page * size,sort,order,name,categoryId);
    }

    @Override
    @Cacheable(cacheNames = "ConcessionsService", key = "'findCountAllConcessionsList_'+#name +'-'+#categoryId")
    public int findCountAll(String name, int categoryId) {
        return this.concessionRepository.findCountAll(name,categoryId);
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
        String sql = "Insert into concession(name,price,category_id) values(?,?,?)";
        int id = this.concessionsCustomRepository.insert(concession,sql);
        cacheManager.clearCache("ConcessionsService");
        return id;
    }
}
