package com.example.prj2be.service.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerService;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.NoticeBoardFileMapper;
import com.example.prj2be.mapper.cs_qa.CSMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CSService {

   private final CSMapper mapper;
   private final NoticeBoardFileMapper fileMapper;

   public boolean save(CustomerService cs, Member login) {

      cs.setCsWriter(login.getId());

      return mapper.insert(cs) == 1;

   }
   public boolean    validate(CustomerService cs) {
      if (cs == null) {
         return false;
      }

      if (cs.getCsContent() == null || cs.getCsContent().isBlank()) {
         return false;
      }

      if (cs.getCsTitle() == null || cs.getCsTitle().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> list(Boolean orderByNum, Boolean orderByHit, Integer page,
      String keyword, String filter) {

      Map<String, Object> map = new HashMap<>();
      Map<String, Object>  pageInfo = new HashMap<>();

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

      // 정렬된 데이터 조회
      List<CustomerService> csList;
      int from = (page - 1) * 10;
      csList = mapper.selectAll(from, "%" + keyword + "%");

      if (orderByNum != null) {
         if (orderByNum) {
            csList = csList.stream().sorted((a, b) -> b.getId() - a.getId()).toList();
         } else {
            csList = csList.stream().sorted((a, b) -> a.getId() - b.getId()).toList();
         }
      } else if (orderByHit != null) {
         if (orderByHit) {
           csList = csList.stream().sorted((a, b) -> b.getId() - a.getId()).toList();
         } else {
            csList = csList.stream().sorted((a, b) -> a.getId() - b.getId()).toList();
         }
      }
      map.put("csList", csList);
      map.put("pageInfo", pageInfo);

      return map;
   }

   public CustomerService get(Integer id) {
      return mapper.selectById(id);
   }

   public boolean remove(Integer id) {
      return mapper.deleteById(id) == 1;
   }

   public boolean update(CustomerService cs, List<Integer> fileSwitch, MultipartFile[] uploadFiles) {
      return mapper.update(cs) == 1;
   }

   public boolean hasAccess(Integer id, Member login) {

      if (login == null) {
         return false;
      }

      if (login.isAdmin()) {
         return true;
      }

      CustomerService cs = mapper.selectById(id);

      return cs.getCsWriter().equals(login.getId());
   }

   public void csHitCount(int id) {

       mapper.increaseHit(id);

   }
}
