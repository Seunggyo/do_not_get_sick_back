package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.mapper.drug.DrugMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugMapper mapper;
    public List<Drug> selectByFunction(String function) {
       return mapper.selectByFunction(function);
    }
}
