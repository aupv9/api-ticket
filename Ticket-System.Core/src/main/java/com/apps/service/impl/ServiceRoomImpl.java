package com.apps.service.impl;

import com.apps.domain.entity.Service;
import com.apps.domain.repository.ServiceCustomRepository;
import com.apps.mybatis.mysql.ServiceRepository;
import com.apps.service.ServiceRoom;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceRoomImpl implements ServiceRoom {

    private final ServiceRepository serviceRepository;
    private final ServiceCustomRepository serviceCustomRepository;

    @Override
    public List<Service> findAll(Integer limit, Integer offset, String sort, String order, String search) {
        return this.serviceRepository.findAll(limit,offset,sort,order,search);
    }

    @Override
    public Service findById(Integer id) {
        return this.serviceRepository.findById(id);
    }

    @Override
    public int update(Service service) {
        var services = this.findById(service.getId());
        services.setName(service.getName());
        services.setDescription(service.getDescription());
        services.setThumbnail(service.getThumbnail());
        return this.serviceRepository.update(services);
    }

    @Override
    public int delete(Integer id) {
        var services = this.findById(id);
        return this.serviceRepository.delete(id);
    }

    @Override
    public int insert(Service service) throws SQLException {
        String sql = "Insert into service(name,description,thumbnail) values(?,?,?)";
        return this.serviceCustomRepository.insert(service,sql);
    }
}
