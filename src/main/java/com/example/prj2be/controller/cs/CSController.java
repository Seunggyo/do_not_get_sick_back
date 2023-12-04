package com.example.prj2be.controller.cs;

import com.example.prj2be.domain.cs.CustomerService;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.cs.CSService;
import java.util.List;
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

@RequestMapping("/api/cs")
@RestController
@RequiredArgsConstructor
public class CSController {

   private final CSService service;

   @PostMapping("add")

   public ResponseEntity add(
      @RequestBody CustomerService cs,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }
      if (!service.validate(cs)) {
         return ResponseEntity.badRequest().build();
      }
      if (service.save(cs, login)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @GetMapping("list")

   // add 된 게시판 리스트를 보여주는 로직
   public List<CustomerService> list(
      @RequestParam(value = "t", required = false) Boolean orderByTitle,
      @RequestParam(value = "h", required = false) Boolean orderByHit) {
      return service.list(orderByTitle, orderByHit);
   }

   @GetMapping("id/{id}")
   public CustomerService get(@PathVariable Integer id) {

      // csDTO 대로 쓴 글을 게시판에 전달..
      return service.get(id);
   }

   @DeleteMapping("remove/{id}")
   public ResponseEntity remove(
      @PathVariable Integer id,
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
      @RequestBody CustomerService cs,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
      }

      if (!service.hasAccess(cs.getId(), login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
      }

      if (service.validate(cs)) {
         if (service.update(cs)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.badRequest().build();
      }
   }

   @PutMapping("{id}")
   public void csHitCount(@PathVariable Integer id) {
      service.csHitCount(id);

   }

}
