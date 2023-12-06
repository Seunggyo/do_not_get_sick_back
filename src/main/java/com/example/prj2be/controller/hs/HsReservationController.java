package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.Hs;
import com.example.prj2be.service.hs.HsReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hospital/reservation")
public class HsReservationController {

    private final HsReservationService service;

    @GetMapping("{id}")
    public Hs get(@PathVariable Integer id) {
        return service.get(id);
    }
}
