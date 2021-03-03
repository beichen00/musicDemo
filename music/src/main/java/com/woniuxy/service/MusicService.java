package com.woniuxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.woniuxy.model.Music;

import java.util.List;

public interface MusicService extends IService<Music> {

    void insertMusic(String name);
    void flushBoard();
    List<Music> findMusicBoard();
    void addLikesCount(Integer id);
    void addVisitCount(Integer id);
}
