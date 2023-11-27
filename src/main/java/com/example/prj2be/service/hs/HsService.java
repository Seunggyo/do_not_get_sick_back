package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.mapper.hs.HsMapper;
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

    public boolean update(Hs hs) {
        return mapper.update(hs) == 1;
    }

    public Hs get(Integer id) {
        Hs hs = mapper.selectById(id);
        return hs;
    }

    ;
}
