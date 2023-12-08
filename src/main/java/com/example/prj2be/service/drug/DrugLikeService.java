package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Like;
import com.example.prj2be.mapper.drug.DrugLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugLikeService {

    private final DrugLikeMapper mapper;
    public void update(Like like) {
        //처음 누를 때는 : insert
        // 다시 누르면 : delete

        int count = 0;
        if (mapper.delete(like) == 0) {
            count = mapper.insert(like);
        }
        ;
    }
}
