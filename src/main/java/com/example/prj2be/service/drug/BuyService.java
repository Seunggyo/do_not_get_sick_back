package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class
BuyService {
    private final CartMapper mapper;
    public Buy getBuy(Integer id) {

        return mapper.selectBuyById(id);
    }

}
