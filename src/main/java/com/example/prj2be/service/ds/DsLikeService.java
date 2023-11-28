package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.DsLike;
import com.example.prj2be.mapper.ds.DsLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DsLikeService {

    private final DsLikeMapper mapper;

    public Map<String, Object> update(DsLike like) {

        int count = 0;

        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

        int countLike = mapper.countByMemberId(like.getMemberId());

        return Map.of("like", count == 1, "countLike", countLike);
    }
}
