package com.example.prj2be.domain.cs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.spi.LocaleNameProvider;
import lombok.Data;

@Data
public class CustomerService {

   private Integer id;
   private String csCategory;
   private String csTitle;
   private String csContent;
   private String csWriter;
   private String csNickName;
   private LocalDate inserted;
   private Integer csHit;
}
