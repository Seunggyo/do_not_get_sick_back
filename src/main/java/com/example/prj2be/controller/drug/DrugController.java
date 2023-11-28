package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.service.drug.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug")
public class DrugController {

    private final DrugService service;

    @GetMapping("list")
    public List<Drug> first(String function) {
        return service.selectByFunction(function);

    }

    @PostMapping("add")
    public ResponseEntity add(Drug drug,
                              @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files) throws IOException {


        if (!service.validate(drug)) {
            return ResponseEntity.badRequest().build();
        }
        if (service.save(drug, files)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("drugList")
    public List<Drug> list() {
        return service.drugList();
    }

    @GetMapping("id/{id}")
    public Drug get(@PathVariable Integer id) {
        return service.drugGet(id);
    }
}
