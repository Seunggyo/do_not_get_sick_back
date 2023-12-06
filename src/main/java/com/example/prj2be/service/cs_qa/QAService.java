package com.example.prj2be.service.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerQA;
import com.example.prj2be.domain.member.Member;

import com.example.prj2be.mapper.cs_qa.QAMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QAService {

   private final QAMapper mapper;
//   private final QACommentMapper qaCommentMapper;

   public boolean save(CustomerQA qa, Member login) {
      qa.setQaWriter(login.getId());

      return mapper.insert(qa) == 1;
   }

   public boolean validate(CustomerQA qa) {
      if (qa == null) {
         return false;
      }

      if (qa.getQaContent() == null || qa.getQaContent().isBlank()) {
         return false;
      }

      if (qa.getQaTitle() == null || qa.getQaTitle().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> qaList(Integer page, String keyword) {
      Map<String, Object> map = new HashMap<>();
      Map<String, Object> pageInfo = new HashMap<>();

      int countAll = mapper.countAll("%" + keyword + "%");
      int lastPageNumber = (countAll - 1) / 10 + 1;
      int startPageNumber = (page - 1) / 10 * 10 + 1;
      int endPageNumber = startPageNumber + 9;
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

      int from = (page - 1) * 10;
      map.put("qaList", mapper.selectAll(from, "%" + keyword + "%"));
      map.put("pageInfo", pageInfo);
      return map;
   }

   public CustomerQA get(Integer id) {

      return mapper.selectById(id);

   }
   public boolean remove(Integer id) {
//      // 게시물에 달린 댓글들 지우기
//      commentMapper.deleteByBoardId(id);

      return mapper.deleteById(id) == 1;
   }

   public boolean update(CustomerQA qa) {
      return mapper.update(qa) == 1;
   }

   public boolean hasAccess(Integer id, Member login) {
      if (login == null) {
         return false;
      }

      if (login.isAdmin()) {
         return true;
      }

      CustomerQA qa = mapper.selectById(id);

      return qa.getQaWriter().equals(login.getId());
   }
}