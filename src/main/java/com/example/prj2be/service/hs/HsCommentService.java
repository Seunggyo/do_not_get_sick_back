package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.HsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.hs.HsCommentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HsCommentService {

    private final HsCommentMapper mapper;

    public boolean add(HsComment comment, Member login) {
        comment.setMemberId(login.getId());
        return mapper.insert(comment) == 1;
    }

    public List<HsComment> list(Integer businessId) {
        return mapper.selectByBusinessId(businessId);
    }

    public boolean validate(HsComment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getBusinessId() == null || comment.getBusinessId() < 1) {
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }
        return true;
    }

    public boolean hasAccess(Integer id, Member login) {
        HsComment comment = mapper.selectById(id);
        if (login == null) {
            return false;
        }
        if (login.getAuth().equals("admin")) {
            return true;
        }
        return comment.getMemberId().equals(login.getId());
    }

    public boolean updateValidate(HsComment comment) {
        if (comment == null) {
            return false;
        }
        if (comment.getId() == null) {
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }
        return true;

    }

    public boolean update(HsComment comment) {

        return mapper.update(comment);
    }

    public boolean remove(Integer id) {

        return mapper.deleteById(id) == 1;
    }

}
