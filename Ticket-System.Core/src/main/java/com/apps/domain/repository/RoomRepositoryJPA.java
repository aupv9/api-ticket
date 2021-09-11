package com.apps.domain.repository;

import com.apps.jpa.entity.Room;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepositoryJPA extends PagingAndSortingRepository<Room,Integer> {
}
