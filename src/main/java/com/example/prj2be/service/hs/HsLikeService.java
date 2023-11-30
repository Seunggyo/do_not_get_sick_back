package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.HsLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.hs.HsLikeMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HsLikeService {

    private final HsLikeMapper mapper;


    public Map<String, Object> update(HsLike like, Member login) {
        like.setMemberId(login.getId());

        int count = 0;
        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }
        int countLike = mapper.countByBoardId(like.getBusinessId());
        return Map.of("like", count == 1, "countLike", countLike);
    }

    public Map<String, Object> get(Integer id, Member login) {
        int countLike = mapper.countByBoardId(id);
        HsLike like = null;
        if (login != null) {
            like = mapper.selectByBusinessIdAndMemberId(id, login.getId());
        }
        return Map.of("like", like != null, "countLike", countLike);
    }
}
