package com.example.prj2be.domain.hs;

import java.util.Date;
import lombok.Data;

@Data
public class HsReservation {

    private Integer id;
    private String name;
    private Integer businessId;
    private String memberId;
    private Date reservationDate;
    private Integer reservationHour;
    private Integer reservationMin;
    private String comment;
}
