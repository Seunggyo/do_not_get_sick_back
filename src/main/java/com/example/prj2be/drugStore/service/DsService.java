package com.example.prj2be.drugStore.service;

import com.example.prj2be.drugStore.domain.Ds;
import com.example.prj2be.drugStore.mapper.DsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (ds.getOpenHour() == null) {
            return false;
        }
        if (ds.getOpenHour() < 0 || ds.getOpenHour() > 23){
            return false;
        }
        if (ds.getOpenMin() == null){
            return false;
        }
        if (ds.getOpenMin() < 0 || ds.getOpenMin() > 60){
            return false;
        }
        if (ds.getCloseHour() == null) {
            return false;
        }
        if (ds.getCloseHour() < 0 || ds.getCloseHour() > 23){
            return false;
        }
        if (ds.getCloseMin() == null){
            return false;
        }
        if (ds.getCloseMin() < 0 || ds.getCloseMin() > 60){
            return false;
        }

        return true;
    }

    public boolean save(Ds ds) {
        // 올바르게 접근한 사용자가 정보 저장 시 db로 정보 보내는 코드
        return mapper.insertById(ds) == 1;
    }
}
