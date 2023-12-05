package com.example.prj2be.controller.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerService;
import com.example.prj2be.domain.cs_qa.QA;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.cs_qa.QA_Service;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/api/cs/qa")
@RestController
@RequiredArgsConstructor
public class QA_Controller {

   private final QA_Service service;

   @PostMapping("add")

   public ResponseEntity add(
      @RequestBody QA qa,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }
      if (!service.validate(qa)) {
         return ResponseEntity.badRequest().build();
      }
      if (service.save(qa, login)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }
   @GetMapping("qa_list")
   public Map<String, Object> list(
      @RequestParam(value = "p", defaultValue = "1") Integer page,
      @RequestParam(value = "k", defaultValue = "") String keyword) {


      return service.qa_list(page, keyword);
   }

   @GetMapping("id/{id}")
   public QA get(@PathVariable Integer id) {
      return service.get(id);
   }
}
