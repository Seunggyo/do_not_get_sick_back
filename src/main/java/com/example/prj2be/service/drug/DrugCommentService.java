package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.DrugCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrugCommentService {

    private final DrugCommentMapper mapper;

    public boolean add(DrugComment drugComment, Member login) {
        drugComment.setMemberId(login.getId());
        return mapper.insert(drugComment) == 1;
    }

    public boolean validate(DrugComment drugComment) {

        if (drugComment == null){
            return false;
        }
        if (drugComment.getDrugId() == null || drugComment.getDrugId() <1) {
            return false;
        }
        if (drugComment.getComment() == null || drugComment.getComment().isBlank()){
            return false;
        }
        return true;
    }

    public List<DrugComment> list(Integer drugId) {
        return mapper.selectByDrugId(drugId);
    }

    public boolean remove(Integer id) {

        return mapper.deleteById(id) == 1;

    }

    public boolean hasAccess(Integer id, Member login) {
        DrugComment drugComment = mapper.selectById(id);

        return drugComment.getMemberId().equals(login.getId());
    }


    public boolean update(DrugComment comment) {
        return mapper.update(comment) == 1;
    }
}
