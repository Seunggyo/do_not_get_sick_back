package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.domain.hs.HsReservation;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.hs.HsReservationService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hospital/reservation")
public class HsReservationController {

    private final HsReservationService service;

    @GetMapping("{id}")
    public Hs get(@PathVariable Integer id) {
        return service.get(id);
    }

    @GetMapping
    public List<HsReservation> reservationGet(@RequestParam("id") String memberId,
        @SessionAttribute(value = "login", required = false) Member login) {
        return service.reservationGet(memberId);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody HsReservation reservation,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.validate(reservation)) {
            if (service.add(reservation, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.isAccess(id, login)) {
            if (service.remove(id)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("check")
    public List<HsReservation> checkGet(@RequestParam("id") String memberId) {
        return service.checkGet(memberId);
    }

    @GetMapping("business/{id}")
    public List<HsReservation> businessGet(@PathVariable("id") Integer memberId) {
        return service.businessGet(memberId);
    }

    @GetMapping("list")
    public List<Hs> businessList(@RequestParam("id") String memberId) {
        return service.memberToBList(memberId);
    }

    @PutMapping("ok/{id}")
    public Integer reservationOk(@PathVariable("id") Integer reservationId) {
        return service.reservationOk(reservationId);
    }

    @GetMapping("business/check/{id}")
    public List<HsReservation> checkGet(@PathVariable("id") Integer businessId) {
        return service.bIdCheckGet(businessId);
    }


    @GetMapping("month")
    public List<HsReservation> month(
        @RequestParam("id") String businessId,
        @RequestParam(value = "startDate", required = false) String start,
        @RequestParam(value = "endDate", required = false) String end) {
        return service.monthCheck(businessId, start, end);
    }
}
