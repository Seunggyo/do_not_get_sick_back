package com.example.prj2be.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Member {
   private String id;
   private String password;
   private String nickName;
   private String phone;
   private String email;
   private String address;
   private String auth;
   private LocalDateTime inserted;

   public boolean isAdmin() {
      if (auth == "admin") {
         return true;
      }
      return false;
   }
}

