package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.DsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.ds.DsCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        return mapper.insert(comment) == 1;
    }

    public Map<String, Object> list(Integer id, Integer page) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageInfo = new HashMap<>();

        // 현재 페이지
        int from = (page - 1) * 5;
        // 총 게시글이 몇개 인지, 어떤 키워드로 검색할 껏인지 등등 총 게시물에서 하는 키워드
        int countAll = mapper.countAll(id);

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

        List<DsComment> dsComments = mapper.selectByBusinessId(from, id);

        map.put("dsComment", dsComments);
        map.put("pageInfo", pageInfo);

        return map;
    }

    public boolean hasAccess(Integer id, Member login) {
        DsComment comment = mapper.selectById(id);

        return comment.getMemberId().equals(login.getId());
    }

    public boolean update(DsComment comment) {
        return mapper.update(comment) == 1;
    }

    public boolean delete(Integer id) {

        return  mapper.deleteById(id) == 1;
    }

    public List<DsComment> listName(String id) {
        return mapper.selectByMemberId(id);
    }

    public Integer getIdByName(String name) {
        return mapper.selectIdByName(name);
    }
}
