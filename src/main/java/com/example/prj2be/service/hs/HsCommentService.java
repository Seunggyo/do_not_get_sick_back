package com.example.prj2be.service.hs;

import com.example.prj2be.domain.hs.HsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.hs.HsCommentMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> list(Integer businessId, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 현재 페이지
        int from = (page - 1) * 5;
        // 총 게시글이 몇개 인지, 어떤 키워드로 검색할 껏인지 등등 총 게시물에서 하는 키워드
        int countAll = mapper.countAll(businessId);

        int lastPageNumber = (countAll - 1) / 5 + 1;
        int startPageNumber = ((page - 1) / 5 * 5) + 1;
        int endPageNumber = startPageNumber + 5;
        endPageNumber = Math.min(endPageNumber, lastPageNumber);

        int prevPageNumber = startPageNumber - 10;
        int nextPageNumber = endPageNumber + 1;

        pageInfo.put("currentPageNumber", page);
        pageInfo.put("startPageNumber", startPageNumber);
        pageInfo.put("endPageNumber", endPageNumber);

        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        List<HsComment> hsComments = mapper.selectByBusinessId(from, businessId);

        map.put("hsComment", hsComments);
        map.put("pageInfo", pageInfo);

        return map;
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
        if (login == null) {
            return false;
        }
        if (login.getAuth() != null) {
            return login.getAuth().equals("admin") == true;
        }
        HsComment comment = mapper.selectById(id);

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
