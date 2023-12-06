package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.mapper.hs.HsReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HsReservationService {

    private final HsReservationMapper mapper;

    public Hs get(Integer id) {
        return mapper.selectByBusinessId(id);
    }
}
