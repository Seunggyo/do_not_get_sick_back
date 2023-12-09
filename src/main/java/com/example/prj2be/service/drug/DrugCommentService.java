package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.CartMapper;
import com.example.prj2be.mapper.drug.DrugCommentMapper;
import com.example.prj2be.mapper.drug.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrugCommentService {

    private final DrugCommentMapper mapper;
    private FileMapper fileMapper;

    public boolean add(DrugComment drugComment, MultipartFile[] files, Member login) {


        drugComment.setMemberId(login.getId());

        int cnt = mapper.insert(drugComment);

                // drugCommentFile 테이블에 files 정보 저장
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // drugCommentId, FileName
                fileMapper.CommentInsert(drugComment.getId(), files[i].getOriginalFilename());

            }
        }

        // 실제 파일을 S3 bucket에 upload

        return cnt == 1;
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

    public boolean updateValidate(DrugComment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getId() == null){
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()){
            return false;
        }
        return true;
    }
}
