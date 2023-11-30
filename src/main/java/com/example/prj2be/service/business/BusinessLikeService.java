package com.example.prj2be.service.business;

import com.example.prj2be.domain.business.BusinessLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.business.BusinessLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BusinessLikeService {

    private final BusinessLikeMapper mapper;

    public Map<String, Object> update(BusinessLike like, Member login) {

        like.setMemberId(login.getId());

        int count = 0;
        // 처음 누르면 insert
        // 두번 누르면 delete
        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

        int countLike = mapper.countByMemberId(like.getBusinessId());

        return Map.of("like", count == 1, "countLike", countLike);
    }

    public Map<String, Object> get(Integer id, Member login) {

        int countLike = mapper.countByMemberId(id);

        BusinessLike like = null;

        if ( login != null){
            like = mapper.selectByDsIdAndMemberId(id, login.getId());
        }

        return Map.of("like", like != null, "countLike", countLike);
    }
}
