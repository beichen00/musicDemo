package com.woniuxy.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.woniuxy.mapper.MusicMapper;
import com.woniuxy.model.Music;
import com.woniuxy.service.MusicService;
import com.woniuxy.utils.ComparableToMusicBoard;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MusicServiceImp extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Resource
    private MusicMapper musicMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void insertMusic(String name) {
        //新增音乐,存入数据库
        Music music = Music.builder().name(name).createTime(System.currentTimeMillis())
                .likesCount(0).visitCount(0).build();
        musicMapper.insert(music);
        //加入redis
        stringRedisTemplate.opsForHash().put("music:id"+music.getId(),
                "likesCount",music.getLikesCount().toString());
        stringRedisTemplate.opsForHash().put("music:id"+music.getId(),
                "visitCount",music.getVisitCount().toString());
        stringRedisTemplate.opsForSet().add("musicIds",music.getId().toString());
    }

    //刷新一下，并从redis缓存里面取出音乐数据，进行排序
    @Override
    public void flushBoard() {
        //查询所有音乐id，然后根据id查询点赞数和浏览量，最后进行比较
        ArrayList<ComparableToMusicBoard> comparableToMusicBoards = new ArrayList<>();
        Set<String> musicIds = stringRedisTemplate.opsForSet().members("musicIds");
        musicIds.forEach(musicId->{
            Object likesCount = stringRedisTemplate.opsForHash().get("music:id" + musicId, "likesCount");
            Object visitCount = stringRedisTemplate.opsForHash().get("music:id" + musicId, "visitCount");
            ComparableToMusicBoard comparableToMusicBoard = ComparableToMusicBoard.builder().id(Integer.valueOf(musicId))
                    .likesCount(Integer.valueOf(likesCount.toString()))
                    .visitCount(Integer.valueOf(visitCount.toString())).build();
            comparableToMusicBoards.add(comparableToMusicBoard);
        });
        Collections.sort(comparableToMusicBoards);
        //将排序后的id存入redis中
        stringRedisTemplate.delete("musicIds");
        int count=0;
        for (int i = 0; i < comparableToMusicBoards.size(); i++) {
            //右进左出
            stringRedisTemplate.opsForList().rightPush("musicIds",comparableToMusicBoards.get(i).getId().toString());

        }
    }

    @Override
    public List<Music> findMusicBoard() {
        //创建集合，接收通过id查询的音乐信息集合
        List<Music> musicList=new LinkedList<>();
        //获取排序后的音乐id集合
        List<String> musicIds = stringRedisTemplate.opsForList().range("musicIds", 0, -1);
        for(String s:musicIds){
            //先将redis中的点赞数和浏览量存入数据库中
            Object likesCount = stringRedisTemplate.opsForHash().get("music:id" + s, "likesCount");
            Object visitCount = stringRedisTemplate.opsForHash().get("music:id" + s, "visitCount");
            Music music = Music.builder().id(Integer.valueOf(s)).visitCount(Integer.valueOf(likesCount.toString()))
                    .visitCount(Integer.valueOf(visitCount.toString())).build();
            musicMapper.updateById(music);

            //然后再从数据库中根据id查询
            QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",Integer.valueOf(s));
            Music selectOneMusic = musicMapper.selectOne(queryWrapper);
            musicList.add(selectOneMusic);
        }
        return musicList;
    }

    //增加点赞数
    @Override
    public void addLikesCount(Integer id) {
        Object likesCount = stringRedisTemplate.opsForHash().get("music:id" + id, "likesCount");
        likesCount=Integer.valueOf(likesCount.toString())+1;
        System.out.println(likesCount+"新的点赞数");
        //重新存入redis中
        stringRedisTemplate.opsForHash().put("music:id"+id,"likesCount",likesCount.toString());

    }

    //增加浏览量
    @Override
    public void addVisitCount(Integer id) {
        Object visitCount = stringRedisTemplate.opsForHash().get("music:id" + id, "visitCount");
        visitCount=Integer.valueOf(visitCount.toString())+1;
        //重新存入redis中
        stringRedisTemplate.opsForHash().put("music:id"+id,"visitCount",visitCount.toString());

    }


}
