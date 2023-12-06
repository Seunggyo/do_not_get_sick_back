package com.example.prj2be.service.cs_qa;

import com.example.prj2be.domain.cs_qa.QA;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.cs_qa.QA_BoardMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QA_Service {

   private final QA_BoardMapper mapper;

   public boolean save(QA qa, Member login) {
      qa.setQa_Writer(login.getId());

      return mapper.insert(qa) == 1;
   }

   public boolean validate(QA qa) {
      if (qa == null) {
         return false;
      }

      if (qa.getQa_Content() == null || qa.getQa_Content().isBlank()) {
         return false;
      }

      if (qa.getQa_Title() == null || qa.getQa_Title().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> qa_list(Integer page, String keyword) {
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
      map.put("boardList", mapper.selectAll(from, "%" + keyword + "%"));
      map.put("pageInfo", pageInfo);
      return map;
   }

   public QA get(Integer id) {

      return mapper.selectById(id);

   }
}