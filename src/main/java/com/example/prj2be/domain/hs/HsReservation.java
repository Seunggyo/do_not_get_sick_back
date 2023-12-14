package com.example.prj2be.domain.hs;

import java.util.Date;
import lombok.Data;

@Data
public class HsReservation {

    private Integer id;
    private String name;
    private Integer businessId;
    private String memberId;
    private String nickName;
    private Date reservationDate;
    private Integer reservationHour;
    private Integer reservationMin;
    private Integer people;
    private String comment;
    private String phone;
    private Boolean isReservationCheck;
}
