package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.DsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.ds.DsCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DsCommentService {

    private final DsCommentMapper mapper;

    public boolean validate(DsComment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getBusinessId() == null || comment.getBusinessId() < 1 ) {
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }
        return  true;
    }

    public boolean add(DsComment comment, Member login) {
        comment.setMemberId(login.getId());
        System.out.println("comment = " + comment);
        System.out.println("login = " + login);

        return mapper.insert(comment) == 1;
    }

    public List<DsComment> list(Integer businessId) {
        return mapper.selectByBusinessId(businessId);
    }

    public boolean hasAccess(Integer id, Member login) {
        DsComment comment = mapper.selectById(id);

        return comment.getMemberId().equals(login.getId());
    }

    public boolean updateValidate(DsComment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getId() == null){
            return false;
        }

        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }

        return true;
    }

    public boolean update(DsComment comment) {
        return mapper.update(comment) == 1;
    }

    public void delete(Integer id) {
        mapper.deleteById(id);
    }

    public List<DsComment> listName(String memberId) {
        return mapper.selectByMemberId(memberId);
    }
}
