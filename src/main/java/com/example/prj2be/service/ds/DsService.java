package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.mapper.ds.DsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DsService {

    private final DsMapper mapper;
    public boolean validate(Ds ds) {
        // 약국 기입시 필수적으로 적어야 하는 목록

        if ( ds == null ){
            return false;
        }
        if (ds.getName().isBlank()) {
            return false;
        }
        if (ds.getAddress().isBlank()) {
            return false;
        }
        if (ds.getPhone().isBlank()) {
            return false;
        }
        if (ds.getContent().isBlank()) {
            return false;
        }

        return true;
    }

    public boolean save(Ds ds) {
        // 올바르게 접근한 사용자가 정보 저장 시 db로 정보 보내는 코드
        return mapper.insertById(ds) == 1;
    }

    public boolean update(Ds ds) {
        // 유저가 정보 수정 할려 할 떄 보내는 코드
        return mapper.updateById(ds) == 1;
    }

    public List<Ds> list( ) {
        return mapper.selectByCategory();
    }

    public Ds get(Integer id) {
        return mapper.selectById(id);
    }


    public boolean delete(Integer id) {
        return mapper.deleteById(id) == 1;
    }
}
