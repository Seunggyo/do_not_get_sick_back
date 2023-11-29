package com.example.prj2be.service.business;

import com.example.prj2be.domain.business.BusinessLike;
import com.example.prj2be.mapper.business.BusinessLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessLikeService {

    private final BusinessLikeMapper mapper;

    public Map<String, Object> update(BusinessLike like) {

        int count = 0;

        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

        int countLike = mapper.countByMemberId(like.getMemberId());

        return Map.of("like", count == 1, "countLike", countLike);
    }
}
