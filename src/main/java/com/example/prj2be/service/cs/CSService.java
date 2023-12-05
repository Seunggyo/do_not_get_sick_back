package com.example.prj2be.service.cs;

import com.example.prj2be.domain.cs.CustomerService;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.cs.CSMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSService {

   private final CSMapper mapper;

   public boolean save(CustomerService cs, Member login) {

      cs.setCsWriter(login.getId());

      return mapper.insert(cs) == 1;

   }
   public boolean validate(CustomerService cs) {
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
      String keyword) {

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

      if (orderByNum != null) {
         if (orderByNum) {
            map.put("csList", mapper.selectAllOrderByNumDesc());
         } else {
            map.put("csList", mapper.selectAllOrderByNumAsc());
         }
      }
      if (orderByHit != null) {
         if (orderByHit) {
            map.put("csList", mapper.selectAllOrderByHitDesc());
         } else {
            map.put("csList", mapper.selectAllOrderByHitAsc());
         }

      }
      int from = (page - 1) * 10;
      map.putIfAbsent("csList", mapper.selectAll(from, "%" + keyword + "%"));
      map.put("pageInfo", pageInfo);
      return map;
   }

   public CustomerService get(Integer id) {
      return mapper.selectById(id);
   }

   public boolean remove(Integer id) {
      return mapper.deleteById(id) == 1;
   }

   public boolean update(CustomerService cs) {
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
