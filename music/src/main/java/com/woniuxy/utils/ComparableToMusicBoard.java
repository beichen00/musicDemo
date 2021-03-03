package com.woniuxy.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComparableToMusicBoard implements Comparable<ComparableToMusicBoard> {
    private Integer id;
    private Integer likesCount;
    private Integer visitCount;

    @Override
    public int compareTo(ComparableToMusicBoard o) {
        if (likesCount>o.getLikesCount()){
            return -1;
        }else if(likesCount==o.getLikesCount()){
            if (visitCount>o.getVisitCount()){
                return -1;
            }else if(visitCount==o.getVisitCount()){
                return 0;
            }else {
                return 1;
            }
        }else {
            return 0;
        }

    }
}
