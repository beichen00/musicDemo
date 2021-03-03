package com.woniuxy.controller;

import com.woniuxy.dto.Result;
import com.woniuxy.dto.Status;
import com.woniuxy.model.Music;
import com.woniuxy.service.MusicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;
    @GetMapping("/likesCountChange/{id}")
    public Result likesCountChange(@PathVariable Integer id){

        musicService.addLikesCount(id);
        return new Result(true, Status.OK,"点赞成功");
    }

    @GetMapping("visitCountChange/{id}")
    public Result visitCountChange(@PathVariable Integer id){

        musicService.addVisitCount(id);
        return new Result(true,Status.OK,"浏览+1");
    }

    @GetMapping("flushBoards")
    public Result flushBoards(){
        musicService.flushBoard();
        return new Result(true,Status.OK,"刷新成功");
    }

    @GetMapping("Boards")
    public Result boards(){
        List<Music> musicBoard = musicService.findMusicBoard();
        return new Result(true,Status.OK,"排行耪数据",musicBoard);
    }

    @GetMapping("musicName/{name}")
    public Result insertMusicName(@PathVariable String name){
        musicService.insertMusic(name);
        return new Result(true,Status.OK,"新增音乐成功");
    }
}
