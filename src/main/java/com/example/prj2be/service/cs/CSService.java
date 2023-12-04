package com.example.prj2be.service.cs;

import com.example.prj2be.domain.cs.CustomerService;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.cs.CSMapper;
import java.util.List;
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

   public List<CustomerService> list(Boolean orderByTitle, Boolean orderByHit) {
      System.out.println("orderByTitle = " + orderByTitle);
      System.out.println("orderByHit = " + orderByHit);
      if (orderByTitle != null) {
         if (orderByTitle) {
            return mapper.selectAllOrderByTitleDesc();
         } else {
            return mapper.selectAllOrderByTitleAsc();
         }
      }
      if (orderByHit != null) {
         if (orderByHit) {
            return mapper.selectAllOrderByHitDesc();
         } else {
            return mapper.selectAllOrderByHitAsc();
         }

      }
      return mapper.selectAll();
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
