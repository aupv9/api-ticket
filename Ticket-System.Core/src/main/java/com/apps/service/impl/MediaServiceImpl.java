package com.apps.service.impl;

import com.apps.domain.entity.Media;
import com.apps.domain.repository.MediaCustomRepository;
import com.apps.mybatis.mysql.MediaRepository;
import com.apps.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final MediaCustomRepository mediaCustomRepository;

    @Override
    public int insert(Media media) throws SQLException {
        return this.mediaCustomRepository.insert(media,"insert into " +
                "media(creation_date,start_date,end_date,description," +
                "name,media_type,path) values(?,?,?,?,?,?,?)");
    }

    @Override
    public Media findById(Integer id) {
        return this.mediaRepository.findById(id);
    }

    @Override
    public Media findByPath(String path) {
        return this.mediaRepository.findByPath(path);
    }

}
