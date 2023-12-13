package com.example.prj2be.controller.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerQA;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.cs_qa.QAService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/qa")
@RestController
@RequiredArgsConstructor
public class QAController {

   private final QAService service;

   @PostMapping("add")

   public ResponseEntity add(
      CustomerQA qa,
      @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
      @SessionAttribute(value = "login", required = false) Member login) throws Exception {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }
      if (!service.validate(qa)) {
         return ResponseEntity.badRequest().build();
      }
      if (service.save(qa, files, login)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }
   @GetMapping("qaList")
   public Map<String, Object> list(
      @RequestParam(value = "p", defaultValue = "1") Integer page,
      @RequestParam(value = "k", defaultValue = "") String keyword,
      @RequestParam(value = "f", defaultValue = "") String filter) {


      return service.qaList(page, keyword, "%"+filter+"%");
   }

   @GetMapping("id/{id}")
   public CustomerQA get(@PathVariable Integer id) {
      return service.get(id);
   }

   @DeleteMapping("remove/{id}")
   public ResponseEntity remove(@PathVariable Integer id,
      @SessionAttribute(value = "login", required = false) Member login) {
      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }

      if (!service.hasAccess(id, login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
      }

      if (service.remove(id)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @PutMapping("edit")
   public ResponseEntity edit(
      CustomerQA qa,
      @RequestParam(value = "fileSwitch[]", required = false) List<Integer> fileSwitch,
      @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles,
      @SessionAttribute(value = "login", required = false) Member login) throws Exception{

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }

      if (!service.hasAccess(qa.getId(), login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
      }

      if (service.validate(qa)) {
         if (service.update(qa, fileSwitch, uploadFiles)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.badRequest().build();
      }
   }
}
