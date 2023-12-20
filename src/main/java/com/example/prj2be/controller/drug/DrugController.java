package com.example.prj2be.controller.drug;


import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.service.drug.DrugService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public Map<String, Object> list(@RequestParam(value = "p", defaultValue = "1") Integer page,
                                    @RequestParam(value = "c", defaultValue = "all") String category,
                                    @RequestParam(value = "k", defaultValue = "") String keyword
                                  ) {


        return service.drugList(page, category, keyword);
    }

    @GetMapping("id/{id}")
    public Drug get(@PathVariable Integer id) {
        return service.drugGet(id);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity remove(@PathVariable Integer id){

        if (service.remove(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(Drug drug,
                               @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
                               @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles) throws IOException {

        if (service.validate(drug)) {
            if (service.update(drug, removeFileIds, uploadFiles)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/func/{func}")
    public List<Drug> funcList(@PathVariable String func,
                               @RequestParam(value = "p", defaultValue = "1") Integer page) {

        if (func.equals("stomach")) {
            func = "위 건강";
        }

        return service.selectByFunctionPage(func, page);
    }

}



