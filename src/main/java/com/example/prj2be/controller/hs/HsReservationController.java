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
}
