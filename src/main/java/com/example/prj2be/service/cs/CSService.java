package com.example.prj2be.service.cs;

import com.example.prj2be.domain.cs.CustomerService;
import com.example.prj2be.mapper.cs.CSMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSService {

   private final CSMapper mapper;

   public boolean save(CustomerService cs) {
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

      if (cs.getCsWriter() == null || cs.getCsWriter().isBlank()) {
         return false;
      }

      return true;
   }

   public List<CustomerService> list() {
      return mapper.selectAll();
   }

   public CustomerService get(Integer id) {
      return mapper.selectById(id);
   }
}
