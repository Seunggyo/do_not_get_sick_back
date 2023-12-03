package com.example.prj2be.controller.cs;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSService {

   private final CSMapper mapper;

   public boolean save(CS cs) {
      return mapper.insert(cs) == 1;

   }
   public boolean validate(CS cs) {
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

   public List<CS> list() {
      return mapper.selectAll();
   }

   public CS get(Integer id) {
      return mapper.selectById(id);
   }
}
