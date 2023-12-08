package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.BuyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class
BuyService {
    private final BuyMapper mapper;
    public boolean save(Buy buy, Member login) {

        return mapper.insert(buy) == 1;

    }
    public Buy getBuy(Integer id) {

        return mapper.selectBuyById(id);
    }

}
