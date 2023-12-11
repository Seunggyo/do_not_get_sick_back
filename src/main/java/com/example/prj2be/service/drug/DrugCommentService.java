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

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrugCommentService {

    private final DrugCommentMapper mapper;
    private final FileMapper fileMapper;

    public boolean add(DrugComment drugComment, MultipartFile[] files, Member login) throws IOException {

        drugComment.setMemberId(login.getId());

        int cnt = mapper.insert(drugComment);

        // drugCommentFile 테이블에 files 정보 저장
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // drugCommentId, FileName
                fileMapper.CommentInsert(drugComment.getId(), files[i].getOriginalFilename());

                 // 실제 파일을 S3 bucket에 upload
                // 일단 local에 저장
                upload(drugComment.getId(), files[i]);
            }
        }
        return cnt == 1;
    }

    private void upload(Integer commentId,MultipartFile file) throws IOException {
        // 파일 저장 경로
        // C:\Temp\prj2\댓글 번호\파일명

            File folder = new File("C:\\Temp\\prj2\\" + commentId);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
            file.transferTo(new File(path));


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
