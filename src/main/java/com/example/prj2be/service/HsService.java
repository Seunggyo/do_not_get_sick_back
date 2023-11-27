package com.example.prj2be.service;

import com.example.prj2be.domain.Hs;
import com.example.prj2be.mapper.HsMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HsService {

    private final HsMapper mapper;

    public List<Hs> list(String category) {
        return mapper.selectByCategory(category);
    }

    public boolean add(Hs hs) {
        return mapper.insert(hs) == 1;
    }

    ;
}
