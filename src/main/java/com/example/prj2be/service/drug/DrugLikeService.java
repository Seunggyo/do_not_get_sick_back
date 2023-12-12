package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Like;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.DrugLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrugLikeService {

    private final DrugLikeMapper mapper;

    public Map<String, Object> update(Like like, Member login) {

        like.setMemberId(login.getId());

        // 처음 누를 때는 : insert
        // 다시 누르면 : delete

        int count = 0;
        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }

        int countLike = mapper.countByDrugId(like.getDrugId());

        return Map.of("like", count == 1,
                "countLike", countLike);

    }

    public Map<String, Object> get(Integer drugId, Member login) {

        int countLike = mapper.countByDrugId(drugId);

        Like like = null;
        if (login != null) {
            like = mapper.selectByDrugIdAndMemberId(drugId, login.getId());
        }

        return Map.of("like", like != null, "countLike",countLike);
    }
}
