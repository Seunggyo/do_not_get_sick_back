package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.service.drug.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug")
public class ChoiceController {

    private final DrugService service;
    @GetMapping("list")
    public List<Drug> first (String function) {
       return service.selectByFunction(function);


    }
}
